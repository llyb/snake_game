package org.example.service.Impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer user_id;
    private Integer rating;
    private Integer waitingTime; // 该名玩家的等待时间
}
