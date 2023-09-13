package com.example.Service.pk;

import java.io.IOException;

public interface StartGameService {
    String startGame(Integer aId, Integer a_bot_id, Integer bId, Integer b_bot_id) throws IOException;
}
