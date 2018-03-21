package com.hlj.common.bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 测试实体类，这个随便;
 */
@Entity
public class DemoInfo  implements Serializable{
	private static final Long serialVersionUID = 1L;
	@Id @GeneratedValue
	private Long id;
	private String name;
	private String pwd;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@Override
	public String toString() {
		return "DemoInfo [id=" + id + ", name=" + name + ", pwd=" + pwd + "]";
	}
}
