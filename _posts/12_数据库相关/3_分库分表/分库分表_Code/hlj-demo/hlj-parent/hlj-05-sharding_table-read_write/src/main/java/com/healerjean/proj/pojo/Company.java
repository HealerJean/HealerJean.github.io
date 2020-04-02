package com.healerjean.proj.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Company {

	private Long id;
	private String name;
	private String companyNameEnglish;
	private String status;
	private Date createTime;
	private Date updateTime;
}
