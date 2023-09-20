package com.example.Controller.bot;

import com.example.Service.user.bot.AddBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddBotController {
    @Autowired
    private AddBotService addBotService;

    @PostMapping("/api/user/bot/add/")
    public Map<String, String> addbot(@RequestParam Map<String, String> data) {
        return addBotService.addbot(data);
    }
}
