package com.example.Service.impl.pk;

import com.example.Service.pk.startGameService;
import com.example.consumer.WebSocketServer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class startGameServiceImpl implements startGameService {
    @Override
    public String startGame(Integer aId, Integer bId) throws IOException {
        System.out.println("start game :" + aId + " " + bId);
        WebSocketServer.startGame(aId, bId);
        return "start game success";
    }
}
