package com.example.Service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Mapper.UserMapper;
import com.example.Service.user.account.UserRegisterService;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // 在我们的SecurityConfig中实现了对PasswordEncoder接口的实现
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null || username.trim() == "") {
            map.put("error_message", "用户名不能为空!");
            return map;
        }
        if (password.trim() == "" || confirmedPassword.trim() == "") {
            map.put("error_message", "密码不能为空！");
            return map;
        }
        if(username.length() > 100) {
            map.put("error_message", "用户名的长度不能超过100");
            return map;
        }

        if(password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }

        if(!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致！");
            return map;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username); // 对用户对象进行封装
        List<User> list = userMapper.selectList(queryWrapper);
        if(!list.isEmpty()) { // 说明数据库中已经存在相同用户名的用户了，注册失败
            map.put("error_message", "用户名已存在！");
            return map;
        }

        map.put("error_message", "success");
        // 将当前用户插入数据库中
        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/138476_lg_fb32a51cd4.jpg";
        User user = new User(null, username, encodedPassword, photo);
        userMapper.insert(user);

        return map;
    }
}
