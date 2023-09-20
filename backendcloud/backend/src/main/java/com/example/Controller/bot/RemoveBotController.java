package com.example.Controller.bot;

import com.example.Service.user.bot.RemoveBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveBotController {
    @Autowired
    private RemoveBotService removeBotService;

    @PostMapping("/api/user/bot/delete/")
    public Map<String, String> deletebot(@RequestParam Map<String, String> data) {
        return removeBotService.removebot(data);
    }
}
