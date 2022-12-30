package com.hlj.proj.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyujin
 * @date 2021/6/24  11:16 上午.
 * @description
 */
@Slf4j
public class LogCallTestService {


    public ResponseDTO invokeMethod(RequestDTO requestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setAgeRes(requestDTO.getAge() + System.currentTimeMillis());
        responseDTO.setNameRes(requestDTO.getName() + System.currentTimeMillis());
        int i = 1/0 ;
        return responseDTO;
    }


    @Data
    public static class RequestDTO {
        private String name;
        private Long age;
    }

    @Data
    public static class ResponseDTO {
        private String nameRes;
        private Long ageRes;
    }
}
