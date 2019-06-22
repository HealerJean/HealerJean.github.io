package com.test;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements.IsEmptyTagHandler;
import com.itmyhome.User;


public class UserTest {
	static SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance(); 
	public static void main(String[] args) {
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance(); 

		//getAlluser();
		//getUserById(3);
		//insertUser();
		//updateUser();
		//deleteUser();
		addNoId();
	}

	//查询所有的user
	private static void getAlluser() {
		// TODO Auto-generated method stub
		System.err.println("获得所有的user");
		try {
			List<User> userList = sqlMap.queryForList("getAllUser");
			System.err.println("得到list的大小为"+userList.size());
			for(User user:userList){
				 System.out.println(user.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getUserById(int id){
		User user;
		try {
			user = (User)sqlMap.queryForObject ("getUser", 3);
			System.out.println(user.getName());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void insertUser(){
		User u = new User();
		u.setId(6);
		u.setName("wangwu");
		u.setAge(23);
		
		try {
			sqlMap.insert("insertUser",u);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void updateUser() {
		// TODO Auto-generated method stub
		User u2 = new User();
		u2.setId(3);
		u2.setName("itmyhome2");
		u2.setAge(25);
		try {
			sqlMap.update("updateUser",u2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void deleteUser() {
		// TODO Auto-generated method stub
		try {
			sqlMap.delete("deleteUser", 3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addNoId() {
		// TODO Auto-generated method stub
		User u = new User();
		u.setName("wangwu");
		u.setAge(23);
		
		try {
			sqlMap.insert("addNoId",u);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
