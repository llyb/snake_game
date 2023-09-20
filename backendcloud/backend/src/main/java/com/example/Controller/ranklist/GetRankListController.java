package com.example.Controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.example.Service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRankListController {
    @Autowired
    private GetRankListService rankListService;
    @PostMapping("/api/get/ranklist/")
    public JSONObject getRankList(@RequestParam Map<String, String> data) {
        Integer page = Integer.valueOf(data.get("page"));
        return rankListService.getRankList(page);
    }
}