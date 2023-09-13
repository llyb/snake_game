package com.example.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    // 玩家的id和其实坐标
    private Integer id;
    private Integer bot_id; // 如果botid为-1说明玩家没有选择bot出战，否则选择的是bot
    private String bot_code;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps; // 用来存储玩家的操作

    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells() { // 根据玩家的操作得到蛇的身体
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y)); // 首先将起点加进去
        for (int d : this.steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing(++ step)) { // 如果蛇没有增长，删除最后一位
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepsString() { // 将玩家的操作转为字符串放到数据库中
        StringBuilder res = new StringBuilder(); // 能够进行修改的字符串，线程不安全，效率高
        for (int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}