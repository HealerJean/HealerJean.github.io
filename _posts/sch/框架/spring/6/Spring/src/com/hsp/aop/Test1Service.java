package com.hsp.aop;

public class Test1Service implements TestServiceInter,TestServiceInter2 {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//第一个的接口的方法
	public void sayHello() {
		// TODO Auto-generated method stub
		System.out.println("hi "+name);
	}

	public void sayBye() {
		// TODO Auto-generated method stub
		System.out.println("bye "+name);
	}

}
