package com.example.Controller.pk;

import com.example.Service.impl.pk.ReceiveBotMoveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveServiceImpl receiveBotMove;

    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("userId")));
        Integer direction = Integer.valueOf(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMove.getMove(userId, direction);
    }
}
