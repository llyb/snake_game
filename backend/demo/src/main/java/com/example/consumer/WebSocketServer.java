package com.example.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.example.Mapper.UserMapper;
import com.example.consumer.utils.Game;
import com.example.consumer.utils.JwtAuthentication;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static ConcurrentMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>(); // 玩家匹配池
    private Session session;
    private User user;
    private static UserMapper userMapper;
    @Autowired
    public void setUsaeMapper(UserMapper userMapper)  {
        WebSocketServer.userMapper = userMapper;
    }

    private void startMatching() throws IOException { // 开始匹配
        System.out.println("start matching");

        matchpool.add(this.user);

        while(matchpool.size() >= 2) {
            Iterator<User> it = matchpool.iterator();
            User a = it.next(); User b = it.next();
            matchpool.remove(a); matchpool.remove(b);

            // 匹配成功向两名玩家发送消息
            Game game = new Game(13, 14, 20);
            game.createGameMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap", game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString()); // 找到用户a的连接并发送信息

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap", game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString()); // 找到用户b并发送信息
        }
    }

    private void stopMatching() {
        System.out.println("stop matching");
        matchpool.remove(this.user);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        // 建立连接，先使用userid进行验证身份，后面再使用token验证
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
            startMatching();
        } else {
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