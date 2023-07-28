package com.example.Service.impl.user.account;

import com.example.Service.user.account.UserLoginService;
import com.example.Service.impl.utils.UserDetailImpl;
import com.example.Utils.JwtUtil;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    // 前端用户调用此接口进行登录
    public Map<String, String> getToken(String username, String password) {
        // 将用户名和密码进行封装，里面存储的就是加密后的密码了
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 进行验证是否能进行登录，登录失败会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 将user取出来
        UserDetailImpl loginUser = (UserDetailImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        // 将用户的userid封装成jwt-token
        String jwt = JwtUtil.createJWT(user.getId().toString());

        // 定义返回结果
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;
    }
}
