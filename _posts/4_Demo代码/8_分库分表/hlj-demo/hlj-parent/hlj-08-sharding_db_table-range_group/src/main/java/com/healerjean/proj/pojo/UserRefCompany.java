package com.healerjean.proj.pojo;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName UserRefCompany
 * @date 2020-03-29  20:36.
 * @Description
 */
@Data
public class UserRefCompany {


    /** 主键  */
    private Long userId;
    private String name;
    private String city;
    private String status;

    private Long companyId;
    private String companyName;
    private String companyNameEnglish;


    private Integer avgAge;
    private Integer sumAge;


}
