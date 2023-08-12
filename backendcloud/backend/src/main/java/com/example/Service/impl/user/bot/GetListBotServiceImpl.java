package com.example.Service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Mapper.BotMapper;
import com.example.Service.user.bot.GetListBotService;
import com.example.Service.impl.utils.UserDetailImpl;
import com.example.pojo.Bot;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListBotServiceImpl implements GetListBotService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public List<Bot> getList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", user.getId());

        return botMapper.selectList(wrapper);
    }
}
