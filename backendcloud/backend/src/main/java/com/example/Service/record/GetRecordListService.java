package com.example.Service.record;

import com.alibaba.fastjson2.JSONObject;

public interface GetRecordListService {
    JSONObject getList(Integer page); // 将所有对局信息进行返回
}
