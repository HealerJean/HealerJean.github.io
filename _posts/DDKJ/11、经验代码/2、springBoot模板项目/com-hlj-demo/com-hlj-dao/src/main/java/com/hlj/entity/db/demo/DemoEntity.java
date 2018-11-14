package com.hlj.entity.db.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 测试实体类;
 */
@Entity
@Table(name = "demo_entity")
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "demo 主键")
	private Long id;

	@ApiModelProperty(value = "姓名")
	private String name;

	@ApiModelProperty(value = "年龄")
	private Long age ;

}

/**

 CREATE TABLE `demo_entity` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `name` varchar(255)   DEFAULT NULL,
 `age` bigint(20) DEFAULT '0',
 PRIMARY KEY (`id`)
 );
 */
