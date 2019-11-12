package com.hlj.springboot.dome.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/24  下午7:17.
 * 类描述：
 */
@Entity
@Table(name = "demo_entity_entity")
@Data
@Accessors(chain = true)
public class OtherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}