package com.healerjean.proj.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HealerJean
 * @date 2021/2/20  7:43 下午.
 * @description
 */
@Data
@AllArgsConstructor
public class CustomerMsgDTO {

    private int id;

    private String name;


}