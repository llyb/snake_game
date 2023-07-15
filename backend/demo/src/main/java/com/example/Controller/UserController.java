package com.example.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Mapper.UserMapper;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/all")
    public List<User> getAll() {
        System.out.println(userMapper.selectList(null));
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{userid}")
    public List<User> getuser(@PathVariable int userid) {
        // 实现复杂的查询语句要先封装这个对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询一个范围内的数据
        queryWrapper.ge("id", 2).le("id", 3);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/user/add/{userid}/{username}/{password}")
    public String addsuer(@PathVariable int userid,
                          @PathVariable String username,
                          @PathVariable String password) {
        // 在添加新用户的时候对密码进行加密
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userid, username, encodedPassword);
        userMapper.insert(user);
        return "add user successfully";
    }

    @GetMapping("/user/delete/{userid}")
    public String deleteUSer(@PathVariable int userid) {
        userMapper.deleteById(userid);
        return "delete successfully!";
    }
}