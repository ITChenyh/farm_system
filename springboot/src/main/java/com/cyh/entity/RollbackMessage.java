package com.cyh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

// 回滚消息体
@Data
@AllArgsConstructor
public class RollbackMessage implements Serializable {
    private String orderId;
    private Integer goodsId;
    private Long timestamp;
}