package com.example.Service.impl.user.bot;

import com.example.Mapper.BotMapper;
import com.example.Service.user.bot.AddBotService;
import com.example.Service.impl.utils.UserDetailImpl;
import com.example.pojo.Bot;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddBotServiceImpl implements AddBotService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> addbot(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();

        User user = loginUser.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if(title == null || title.trim().length() == 0) {
            map.put("error_message", "标题不能为空！");
            return map;
        }

        if(title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }

        if(description == null || description.trim().length() == 0) { // 这种情况是合法的，但是我们想给他一个初始值
            description = "这个用户很懒，什么都没留下~";
        }

        if(description.length() > 300) {
            map.put("error_message", "Bot描述的长度不能大于300");
            return map;
        }

        if(content == null || content.trim().length() == 0) {
            map.put("error_message", "代码不能为空！");
            return map;
        }

        if(content.length() > 10000) {
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }

        // 如果没问题将当前bot添加到数据库中
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, now, now);
        botMapper.insert(bot);

        map.put("error_message", "success");

        return map;
    }
}
