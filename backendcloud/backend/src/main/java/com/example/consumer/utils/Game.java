package com.example.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.example.consumer.WebSocketServer;
import com.example.pojo.Bot;
import com.example.pojo.Record;
import com.example.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final int [][] g; // 地图对象
    private final int rows, cols;// 地图的长和宽
    private final int inner_walls_count; // 墙的数量
    private static final int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    // 定义两名玩家的下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private final ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing、finished
    private String loser = ""; // all A B 表示谁输了
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";
    public Game(int rows, int cols, int inner_walls_count, Integer idA, Bot a_bot, Integer idB, Bot b_bot) { // 在一对中，前面的是左下角的，后面的是右上角的
        this.cols = cols;
        this.rows = rows;
        this.g = new int[rows][cols];
        this.inner_walls_count = inner_walls_count;

        Integer a_bot_id = -1, b_bot_id = -1;
        String a_bot_code = "", b_bot_code = "";
        if (a_bot != null) {
            a_bot_id = a_bot.getId();
            a_bot_code = a_bot.getContent();
        }
        if (b_bot != null) {
            b_bot_id = b_bot.getId();
            b_bot_code = b_bot.getContent();
        }
        this.playerA = new Player(idA, a_bot_id, a_bot_code, this.rows - 2, 1, new ArrayList<>()); // a是左下角的蛇，b是右上角的蛇。这里和前端是对应的先左下，后右上
        this.playerB = new Player(idB, b_bot_id, b_bot_code, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    // 从前端获取用户的操作
    public void setnextStepA(Integer nextStepA) {
        // 在外面的线程我们会读取该变量，同时我们也会写此变量，故要加上锁
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return this.g;
    }

    private boolean check_connectivity(int sx, int sy, int ex, int ey) {
        if(sx == ex && sy == ey) return true;
        g[sx][sy] = 1; // 从起点开始走
        for(int i = 0; i < 4; i ++) {
            int a = sx + dx[i], b = sy + dy[i];
            if(a < 0 || a >= this.rows || b < 0 || b >= this.cols || this.g[a][b] == 1) continue;
            if(check_connectivity(a, b, ex, ey)) {
                g[sx][sy] = 0;
                return true;
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {
        for(int i = 0; i < this.rows; i ++) {
            for(int j = 0; j < this.cols; j ++) {
                g[i][j] = 0;
            }
        }

        // 给四周加上墙
        for(int i = 0; i < this.rows; i ++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }
        for(int i = 0; i < this.cols; i ++) {
            g[0][i] = g[this.rows - 1][i] = 1;
        }

        // 随机生成墙
        Random random = new Random();
        for(int i = 0; i < this.inner_walls_count / 2; i ++) {
            for(int j = 0; j < 1000; j ++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        // 检验地图的连通性
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createGameMap() {
        for(int i = 0; i < 1000; i ++) {
            if(this.draw()) {
                break;
            }
        }
    }

    String getInput(Player player) { // 将地图信息进行封装
        Player me, you;
        if (player.getId().equals(playerA.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }

        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")#";
    }

    private void sendBotCode(Player player) { // 将当前玩家bot的代码发送到代码执行的位置
        // 首先判断是机器操作还是人来操作
        if (player.getBot_id().equals(-1)) return; // 说明是人操作直接进行返回
//        System.out.println("bot_id：" + player.getId().toString() + "bot code：" + player.getBot_code() + "当前局面信息：" + getInput(player));
        // 否则将代码发送到代码执行微服务中
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBot_code());
        data.add("input", getInput(player)); // 存储当前的局面信息
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    private boolean nextStep() { // 等待两名玩家的下一步操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 机器输入的接口，人输入的接口在WebsocketServer部分
        sendBotCode(playerA);
        sendBotCode(playerB);

        // 我们可以等待5秒钟，如果用户或者是机器没有输入则输
        for (int i = 0; i < 50; i ++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (this.nextStepA != null && this.nextStepB != null) { // 如果两名玩家的下一步操作都不为空说明准备好了
                        playerA.getSteps().add(this.nextStepA);
                        playerB.getSteps().add(this.nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private void sendAllMessage(String message) throws IOException {
        if(WebSocketServer.users.get((playerA.getId())) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                res.append(g[i][j]);
                System.out.printf("%d ", g[i][j]);
            }
            System.out.println("");
        }
        return res.toString();
    }
    private void updateUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void saveToDatabase() {
        // 首先找到两名玩家的rating
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if ("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;
        } else if ("B".equals(loser)) {
            ratingA += 5;
            ratingB -= 2;
        }

        updateUserRating(playerA, ratingA);
        updateUserRating(playerB, ratingB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() throws IOException { // 向两个用户广播游戏结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase(); // 游戏结束，将游戏存储到数据库中
        sendAllMessage(resp.toJSONString());
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false; // 如果走到墙上了

        for (int i = 0; i < n - 1; i ++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) return false;
        }

        for (int i = 0; i < n - 1; i ++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) return false;
        }

        return true;
    }

    private void judge() { // 判断蛇的行为是否合法，是否撞墙了
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) { // 如果二者都是非法的，那么游戏结束
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else if (!validB) {
                loser = "B";
            }
        }
    }

    private void sendMove() throws IOException { // 广播继续移动的信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++) {
            if(nextStep()) { // 如果两名玩家都准备好了
                judge();
                if(status.equals("playing")) { // 正常游戏
                    try {
                        sendMove();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else { // 撞墙了
                    try {
                        sendResult();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            } else { // 游戏结束
                lock.lock();
                try {
                    this.status = "finished"; // 当前线程的游戏结束
                    if (nextStepA == null &&nextStepB == null) {
                        this.loser = "all";
                    } else if(nextStepA == null) {
                        this.loser = "A";
                    } else if(nextStepB == null) {
                        this.loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                try {
                    sendResult();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}
