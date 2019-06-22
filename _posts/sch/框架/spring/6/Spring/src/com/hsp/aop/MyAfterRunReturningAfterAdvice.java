package com.hsp.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;


public class MyAfterRunReturningAfterAdvice implements AfterReturningAdvice {

	/**
	 * 
	 */
	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {
		System.out.println("****************************");

		System.out.println("关闭资源"+arg1.getName());

		// TODO Auto-generated method stub
		
	}

}
