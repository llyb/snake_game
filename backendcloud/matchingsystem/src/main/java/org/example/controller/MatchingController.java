package org.example.controller;

import org.example.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    @PostMapping("/add/match/player/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.valueOf(Objects.requireNonNull(data.getFirst("rating")));
        Integer bot_id = Integer.valueOf(Objects.requireNonNull(data.getFirst("bot_id")));
        return matchingService.addPlayer(userId, bot_id, rating);
    }

    @PostMapping("/remove/match/player/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}