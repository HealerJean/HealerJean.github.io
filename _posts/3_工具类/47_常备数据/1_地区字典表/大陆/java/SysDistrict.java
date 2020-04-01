package com.healerjean.proj.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author HealerJean
 * @ClassName SysDistrict
 * @date 2020/4/1  10:13.
 * @Description
 */
@Data
public class SysDistrict {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String districtCode;
    private String districtName;
    private String status;
    private Date createTime;
    private Date updateTime;
}


//
//     create table `sys_district`
//         (
//         `id`            bigint(20) unsigned not null auto_increment comment '主键',
//         `province_code` varchar(8)                   default '' comment '省-编码',
//         `province_name` varchar(64)                  default '' comment '省-名称',
//         `city_code`     varchar(8)                   default '' comment '城市-编码',
//         `city_name`     varchar(64)                  default '' comment '城市-名称',
//         `district_code` varchar(8)                   default '' comment '区/县-编码',
//         `district_name` varchar(64)                  default '' comment '区/县-名称',
//         `status`        int(10)             not null default '0' comment '状态',
//         `create_time`   datetime            not null default current_timestamp comment '创建时间',
//         `update_time`   datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
//         primary key (`id`) using btree,
//         key `idx_province` (`province_code`) using btree comment '省份-索引',
//         key `idx_city` (`city_code`) using btree comment '城市-索引',
//         key `idx_district` (`district_code`) using btree comment '地区-索引'
//         ) engine = innodb
// default charset = utf8 comment ='系统模块-地区信息表';

