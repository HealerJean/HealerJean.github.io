package com.healerjean.proj.kafka.message;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemoKMsg {

    private Long id;    //id

    private String msg; //消息

    private LocalDateTime sendTime;  //时间戳

}
