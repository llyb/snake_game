package org.com.example;

import org.com.example.Service.Impl.BotRunningServiceImpl;
import org.com.example.Service.Impl.utils.BotPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotrunningApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.botPool.start();
        SpringApplication.run(BotrunningApplication.class, args);
    }
}