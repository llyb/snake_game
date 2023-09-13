package com.example.Controller.pk;

import com.example.Service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class StartGameController {
    @Autowired
    private StartGameService startgameservice;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String,String> data) throws IOException {
        Integer aId = Integer.valueOf(data.getFirst("a_id"));
        Integer a_bot_id = Integer.valueOf(data.getFirst("a_bot_id"));
        Integer bId = Integer.valueOf(data.getFirst("b_id"));
        Integer b_bot_id = Integer.valueOf(data.getFirst("b_bot_id"));
        return startgameservice.startGame(aId, a_bot_id, bId, b_bot_id);
    }
}
