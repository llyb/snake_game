package com.example.Controller.user.account;

import com.example.Service.user.account.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRegisterController {
    @Autowired
    private UserRegisterService userRegisterService;

    @PostMapping("user/account/register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassoword = map.get("confirmedPassoword");
        return userRegisterService.register(username, password, confirmedPassoword);
    }
}
