package cn.bjsxt.oop.testStatic;

public class Student {
	 String name;
	 int id;   
	 
	 static  int  ss;
	 
	 public static void printSS(){
		 System.out.println(ss);
	 }
	 

	 public void study(){
		 printSS();
		 System.out.println(name+"�ڌW��");
	 }
	 
	 public void sayHello(String sname){
		 System.out.println(name+"��"+sname+"�f����ã�");
	 }
	
}



