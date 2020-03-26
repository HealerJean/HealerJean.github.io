package com.healerjean.proj.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName UserDTO
 * @date 2020/3/5  16:10.
 * @Description
 */
@Data
public class UserDTO {

    private Long id;
    private Long userId;
    private List<Long> ids ;
    private String name;
    private Integer age;
    private String email;


    /** 添加日期进行测试 */
    private LocalDate createDate;
    private LocalDateTime createTime;

}
