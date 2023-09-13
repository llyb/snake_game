package com.example.Service.impl.pk;

import com.example.Service.pk.StartGameService;
import com.example.consumer.WebSocketServer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer a_bot_id, Integer bId, Integer b_bot_id) throws IOException {
        System.out.println("start game :" + aId + " " + bId);
        WebSocketServer.startGame(aId, a_bot_id, bId, b_bot_id);
        return "start game success";
    }
}
