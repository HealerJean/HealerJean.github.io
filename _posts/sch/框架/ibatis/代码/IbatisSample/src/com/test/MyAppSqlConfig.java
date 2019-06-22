package com.test;

import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class MyAppSqlConfig {
	
	private static final SqlMapClient sqlMap;
	static {
		try {
			String resource = "SqlMapConfig.xml";
			Reader reader = Resources.getResourceAsReader(resource); //读取配置文件 
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing MyAppSqlConfig class. Cause: " + e);
		}
	}
	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}
}