package com.example.Controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.example.Service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordListService recordListService;

    @GetMapping("/api/record/getList/")
    public JSONObject getList(Integer page) {
        return recordListService.getList(page);
    }
}