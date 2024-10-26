package com.example.Service.impl.user.account.acwing;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Mapper.UserMapper;
import com.example.Service.impl.user.account.acwing.utils.HttpClientUtil;
import com.example.Service.user.account.acwing.AcappService;
import com.example.Utils.JwtUtil;
import com.example.pojo.User;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class AcappServiceImpl implements AcappService {
    private final static String appId = "6053";
    private final static String appSecrete = "1041b06904c24ac4b0af12c6cba1bada";
    private final static String redirectUri = "https://app6053.acapp.acwing.com.cn/api/user/account/acwing/acapp/receive_code/";
    private final static String applyAccessTokenUrl = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    private final static String getUserInfoUrl = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
    private final static Random random = new Random();
    private final static SecureRandom random1 = new SecureRandom();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final int PASSWORD_LENGTH = 12; // 设置密码长度
    @Override
    public JSONObject applyCode() { // 对申请授权码code请求的封装
        JSONObject resp = new JSONObject();
        resp.put("appid", appId);
        try {
            resp.put("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result", "failed");
            return resp;
        }
        resp.put("scope", "userinfo");
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 10; i ++)
            state.append(random.nextInt(10) + '0');

        resp.put("state", state);
        resp.put("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
        return resp;
    }

    @Override
    public JSONObject receiveCode(String code, String state) { // 执行二三步（当第一步成功后会执行）
        JSONObject resp = new JSONObject();
        resp.put("result", "failed");
        if (code == null || state == null) return resp;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) return resp;
        redisTemplate.delete(state);
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", appId));
        nameValuePairs.add(new BasicNameValuePair("secret", appSecrete));
        nameValuePairs.add(new BasicNameValuePair("code", code));

        // 得到返回结果
        String getString = HttpClientUtil.get(applyAccessTokenUrl, nameValuePairs);
        if (getString == null) return resp;
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
        if (accessToken == null || openid == null) return resp;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            User user = users.get(0);
            String jwt_token = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt_token);
            return resp;
        }

        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(getUserInfoUrl, nameValuePairs);
        if (getString == null) return resp;
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");

        if (username == null || photo == null) return resp;
        // 为了保证用户名不相同：如果存在重复则在新建用户名后随机加一位
        for (int i = 0; i < 100; i ++) {
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("username", username);
            if (userMapper.selectList(queryWrapper1).isEmpty()) break;
            username += (char)(random.nextInt(10) + '0');
            if (i == 99) return resp;
        }

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i ++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(
                null,
                username,
                encodedPassword,
                photo,
                1500,
                openid
        );
        userMapper.insert(user);
        String jwt_token = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt_token);
        return resp;
    }
}
