package org.example;

import org.example.service.Impl.MatchingServiceImpl;
import org.example.service.Impl.utils.MatchingPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start(); // 开启我们的匹配线程
        SpringApplication.run(MatchingSystemApplication.class, args);
    }
}