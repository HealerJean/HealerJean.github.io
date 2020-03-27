package com.healerjean.proj.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyDTO {
	private Long id;
	private String name;
	private String companyNameEnglish;
	private String status;
	private Date createTime;
	private Date updateTime;
}
