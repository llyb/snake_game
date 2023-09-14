package org.com.example.Service.Impl;

import org.com.example.Service.BotRunningService;
import org.com.example.Service.Impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool = new BotPool(); // 开启一个全局代码执行消费者线程
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        botPool.addBot(userId, botCode, input);
        System.out.println("add Bot: " + userId + " " + botCode + " " + input);
        return "add bot success!";
    }
}