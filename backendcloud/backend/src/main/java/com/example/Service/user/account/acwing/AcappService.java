package com.example.Service.user.account.acwing;

import com.alibaba.fastjson2.JSONObject;

public interface AcappService {
    JSONObject applyCode();
    JSONObject receiveCode(String code, String state); // 接受服务器发送的结果
}
