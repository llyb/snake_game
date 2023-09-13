package org.example.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer bot_id, Integer rating);
    String removePlayer(Integer userId);
}
