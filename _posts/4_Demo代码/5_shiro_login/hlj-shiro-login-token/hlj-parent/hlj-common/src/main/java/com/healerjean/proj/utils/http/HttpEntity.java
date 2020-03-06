package com.healerjean.proj.utils.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpEntity {

    /**
     * Http返回码
     */
    private int code;
    /**
     * Http返回正文
     */
    private String content;
}
