package com.hlj.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCache {

	
	//ʹ��map��ģ�⻺��
	static Map<Integer,Student> maps=new HashMap<Integer,Student>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		getStudent(1);
		getStudent(1);
		getStudent(1);
		getStudent(1);
		getStudent(3);
		getStudent(3);
		
		
		
	}
	
	public static Student getStudent(Integer id){  //s.get()
		
		
		//�ȵ�����ȥ
		if(maps.containsKey(id)){
			//�ڻ�����
			System.out.println("从缓存中读取");
			return maps.get(id);
		}else{
			System.out.println("从数据库中读取");
			//����ݿ�ȡ
			Student stu=MyDB.getStudentFromDB(id);
			//���뻺��
			maps.put(id, stu);
			return stu;
		}
		
	
		
	}

} 

//�ҵ���ݿ�
class MyDB{
	
	static List<Student> lists=new  ArrayList<Student>();
	
	//��ʼ����ݿ�,���������ѧ��
	static{
		Student s1=new Student();
		s1.setId(1);
		s1.setName("aaa");
		Student s2=new Student();
		s2.setId(2);
		s2.setName("bbb");
		Student s3=new Student();
		s3.setId(3);
		s3.setName("ccc");
		lists.add(s1);
		lists.add(s2);
		lists.add(s3);
		
	}
	
	public static Student getStudentFromDB(Integer id){
		for(Student s: lists){
			if(s.getId().equals(id)){
				return s;
			}
		}
		return null;// ����ݿ���û��.
		
	}
}

class Student{
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
