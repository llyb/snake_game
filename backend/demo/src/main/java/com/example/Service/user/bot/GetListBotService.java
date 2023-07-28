package com.example.Service.user.bot;

import com.example.pojo.Bot;

import java.util.List;
import java.util.Map;

public interface GetListBotService {
    List<Bot> getList(); // 根据用户的id查询，所以不用传参
}
