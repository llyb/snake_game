package com.example.Service.impl.user.bot;

import com.example.Mapper.BotMapper;
import com.example.Service.user.bot.RemoveBotService;
import com.example.Service.impl.utils.UserDetailImpl;
import com.example.pojo.Bot;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveBotServiceImpl implements RemoveBotService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> removebot(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUSer = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUSer.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id); // 用来检验该bot是否存在
        Map<String, String> map = new HashMap<>();

        if(bot == null) {
            map.put("error_message", "bot不存在或已被删除");
            return map;
        }

        if(bot.getUserId() != user.getId()) {
            map.put("error_message", "没有权限删除该bot");
            return map;
        }

        botMapper.deleteById(bot_id);

        map.put("error_message", "success");

        return map;
    }
}
