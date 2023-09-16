package com.example.Service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Mapper.RecordMapper;
import com.example.Mapper.UserMapper;
import com.example.Service.record.GetRecordListService;
import com.example.pojo.Record;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    public RecordMapper recordMapper;
    @Autowired
    public UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) { // 将所有的对局信息进行返回
        JSONObject resp = new JSONObject();
        // 得到当前page的信息，并将所有的信息按照id递减的顺序进行返回
        IPage<Record> recordIPage = new Page<>(page, 10); // 获取第几页，每页有多少行记录
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();

        // 将当前页的信息存放到items中发送给前端
        List<JSONObject> items = new LinkedList<>();
        for (Record record: records) {
            JSONObject item = new JSONObject(); // 存储的是一条记录的信息
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String result = "平局";
            if ("A".equals(record.getLoser())) result = "B胜";
            else if ("B".equals(record.getLoser())) result = "A胜";
            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("records_count", recordMapper.selectCount(null));
        return resp;
    }
}