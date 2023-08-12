package com.example.Controller.pk;

import com.example.Service.pk.startGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class startGameController {
    @Autowired
    private startGameService startgameservice;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String,String> data) throws IOException {
        Integer aId = Integer.valueOf(data.getFirst("a_id"));
        Integer bId = Integer.valueOf(data.getFirst("b_id"));
        return startgameservice.startGame(aId, bId);
    }
}
