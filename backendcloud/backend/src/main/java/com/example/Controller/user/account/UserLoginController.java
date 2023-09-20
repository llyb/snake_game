package com.example.Controller.user.account;

import com.example.Service.user.account.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserLoginController {
    @Autowired
    private UserLoginService loginService;

    @PostMapping("/api/user/account/token/")
    private Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username, password); // 在登录的时候如果成功那么要返回jwt-token了
    }
}
