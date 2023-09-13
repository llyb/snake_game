package com.example.Service.impl.pk;

import com.example.Service.pk.ReceiveBotMoveService;
import com.example.consumer.WebSocketServer;
import com.example.consumer.utils.Game;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String getMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction + " ");
        if (WebSocketServer.users.get(userId) != null) { // 如果存在当前用户
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (Objects.equals(game.getPlayerA().getId(), userId)) { // 说明当前用户是A
                    game.setnextStepA(direction);
                } else if (Objects.equals(game.getPlayerB().getId(), userId)) { // 如果当前用户是B
                    game.setNextStepB(direction);
                }
            }
        }
        return "receive bot move success";
    }
}