package org.example.service.Impl;

import org.example.service.Impl.utils.MatchingPool;
import org.example.service.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating) { // 在这里面调用
        System.out.println(userId + ' ' + rating);
        matchingPool.addPlayer(userId, rating);
        return "add success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println(userId);
        matchingPool.removePlayer(userId);
        return "remove success";
    }
}
