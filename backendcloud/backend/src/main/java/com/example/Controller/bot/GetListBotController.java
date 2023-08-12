package com.example.Controller.bot;

import com.example.Service.user.bot.GetListBotService;
import com.example.pojo.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListBotController {
    @Autowired
    private GetListBotService getListBotService;

    @GetMapping("/user/bot/getlist")
    public List<Bot> getlist() {
        return getListBotService.getList();
    }
}
