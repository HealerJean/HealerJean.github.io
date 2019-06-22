package com.hlj.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hlj.service.BybService;
import com.hlj.service.UserService;
import com.util.ApplicaionContextUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//������ʹ�ô�ͳ�ķ�����������UserService�� sayHello����
	/*	UserService userService=new UserService();
		userService.setName("woshi ");
		userService.sayHello();
		*/
		//使用spring来完成上面的任务
		//得到spring 对象中的容器对象applicationContext.xml
 
	  ApplicationContext ac = ApplicaionContextUtil.getApplicationContext();
	  //得到userService 这个对象
	  	UserService us=(UserService)ac.getBean("userService");
	  	us.sayHello(); 
			BybService bybService=(BybService) ac.getBean("bybService");
			bybService.sayBye(); 
	   
		//UserService us=(UserService) ac.getBean("userService");
		//us.sayHello();
//		((UserService)ApplicaionContextUtil.getApplicationContext()
//		.getBean("userService")).sayHello();
		
		//��ac[���applicatonContext����]��
	//	BybService bybService=(BybService) ac.getBean("bybService");
	//	bybService.sayBye();
		
	}

}
