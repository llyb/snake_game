package org.example.service.Impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread { // 这里实现对玩家进行匹配并向后端发送匹配消息
    private static List<Player> players = new ArrayList<>(); // 用来存储所有的匹配玩家
    private final ReentrantLock lock = new ReentrantLock(); // 定义锁
    private static RestTemplate restTemplate; // 用于传递消息
    @Autowired
    private void serRestTemlate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    public void addPlayer(Integer user_id, Integer rating) { // 在匹配池中添加一名玩家
        lock.lock();
        try {
            for (Player player : players) {
                if (player.getUser_id().equals(user_id)) { // 如果存在当前id的用户将他删除后在加进来
                    players.remove(player);
                }
            }
            players.add(new Player(user_id, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer user_id) { // 移除当前id的玩家
        lock.lock();
        try {
            List<Player> new_players = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUser_id().equals(user_id)) {
                    new_players.add(player);
                }
            }
            players = new_players;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() { // 增加玩家的等待时间
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(Player a, Player b) { // 这里的策略是阈值能同时满足两名玩家的分值
        int ratingDelta = Math.abs(a.getRating() - b.getRating()); // 计算二者相差的分值
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void sendresult(Player a, Player b) { // 匹配成功向server发送消息
        System.out.println("send result: " + a  + ' ' + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUser_id().toString());
        data.add("b_id", b.getUser_id().toString());
        restTemplate.postForObject(startGameUrl, data, String.class); // 最后一个参数是返回值类型的反射
    }

    private void matchPlayers() { // 对玩家进行匹配，每次匹配一对
        System.out.println("matchPlayers " + players.toString());
        // 优先匹配等待时间长的玩家，也就是在数组前面的玩家
        boolean[] used = new boolean[players.size()]; // 用于存储所有已经完成配对的玩家
        for (int i = 0; i < players.size(); i ++) {
            if(used[i]) continue;
            for (int j = i + 1; j < players.size(); j ++) {
                if(used[j]) continue;
                Player a = players.get(i), b = players.get(j); // 得到两名没有进行匹配的玩家
                if(checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendresult(a, b);
                    break;
                }
            }
        }

        // 将匹配过的玩家删除
        List<Player> new_players = new ArrayList<>();
        for (int i = 0; i < players.size(); i ++) {
            if (!used[i]) new_players.add(players.get(i));
        }
        players = new_players;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000); // 每隔1s进行对玩家进行匹配判断
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}
