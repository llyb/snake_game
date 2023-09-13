package com.example.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.example.Mapper.BotMapper;
import com.example.Mapper.RecordMapper;
import com.example.Mapper.UserMapper;
import com.example.consumer.utils.Game;
import com.example.consumer.utils.JwtAuthentication;
import com.example.pojo.Bot;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static ConcurrentMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private Session session;
    private User user;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static BotMapper botMapper;
    public static RestTemplate restTemplate;
    public Game game = null; // 用于存储当前的游戏信息
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }
    // 定义通信的url链接
    private final String addPlayerUrl = "http://127.0.0.1:3001/add/match/player/";
    private final String removePlayerUrl = "http://127.0.0.1:3001/remove/match/player/";

    public static void startGame(Integer aId, Integer a_bot_id, Integer bId, Integer b_bot_id) throws IOException {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        Bot a_bot = botMapper.selectById(a_bot_id);
        Bot b_bot = botMapper.selectById(b_bot_id);

        // 匹配成功向两名玩家发送消息
        Game game = new Game(13, 14, 20, a.getId(), a_bot, b.getId(), b_bot);
        game.createGameMap(); // 首先创建地图
        game.start(); // 启动一个新的线程

        // 分别对a和b中的game对象进行赋值
        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if(users.get(a.getId()) != null)
            users.get(b.getId()).game = game;

        // 将玩家的位置和地图进行封装
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", a.getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", b.getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("gamemap", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame); // 将地图信息传给A
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString()); // 找到用户a的连接并发送信息

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame); // 将地图信息传给B
        if(users.get(a.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString()); // 找到用户b并发送信息
    }

    private void startMatching(Integer bot_id) throws IOException { // 开始匹配，向我们的matchingsystem发送一个请求添加一名玩家到匹配池中
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap();
        data.add("user_id", user.getId().toString());
        data.add("rating", user.getRating().toString());
        data.add("bot_id", bot_id.toString());
        // 将当前玩家发送到匹配系统中
        restTemplate.postForObject(addPlayerUrl, data, String.class); // 最后一个参数填返回值的反射
    }

    private void stopMatching() { // 向matchingsystem发送请求，表示取消这名玩家的匹配
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap();
        data.add("user_id", user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(Integer direction) { // 设定两名玩家的游戏方向
        if (Objects.equals(game.getPlayerA().getId(), user.getId())) { // 说明当前用户是A
            if (!game.getPlayerA().getBot_id().equals(-1)) // 如果不是机器操作才传入人的操作
                game.setnextStepA(direction);
        } else if(Objects.equals(game.getPlayerB().getId(), user.getId())) {
            if (!game.getPlayerB().getBot_id().equals(-1))
                game.setNextStepB(direction);
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        // 建立连接，使用jwt-token进行身份验证
        int userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if(this.user != null) { // 说明验证成功
            System.out.println("connected!");
            users.put(userId, this);
        } else { // 验证失败,关闭当前会话
            this.session.close();
        }
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println("disconnetced!");
        if(this.user != null) {
            users.remove(this.user);
        }
        this.session.close();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException { // 充当路由，根据前端的不同消息执行不同的函数
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message); // 将json字符串转化为json对象进而得到相关信息
        String event = data.getString("event"); // 将前端传来的数据进行解析
        if("start-matching".equals(event)) {
            System.out.println(data.getInteger("bot_id"));
            startMatching(data.getInteger("bot_id"));
        } else if("move".equals(event)) {
//            System.out.println(event + " " + data.getInteger("direction"));
            move(data.getInteger("direction"));
        } else if("stop-matching".equals(event)) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException { // 自定义了一个函数用来向前端发送消息
        synchronized (this.session) {
            this.session.getBasicRemote().sendText(message);
        }
    }
}