package com.hlj.proj.data.pojo.demo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 测试实体类;
 */
@Entity
@Table(name = "demo_entity")
@Data
@Accessors(chain = true)
public class DemoEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Long age ;

	@Column(insertable = true,updatable = false)
	private Date cdate;

	private Date udate;

}

/**

 drop table demo_entity;

 CREATE TABLE `demo_entity` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `name` varchar(255)   DEFAULT NULL,
 `age` bigint(20) DEFAULT '0',
 `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`)
 );
 */
