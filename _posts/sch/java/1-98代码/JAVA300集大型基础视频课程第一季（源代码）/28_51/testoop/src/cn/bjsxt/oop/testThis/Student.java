package cn.bjsxt.oop.testThis;

public class Student {
	 String name;
	 int id;   
	 
	 public  Student(String name,int id){
		 this(name);   //ͨ��this�����������췽��������λ�ڵ�һ�䣡 Constructor call must be the first statement in a constructor
		 this.name = name;
		 this.id = id;
	 }
	 
	 public Student(String name){
		 this.name = name;
	 }
	 public Student(){
		 System.out.println("����һ������");
	 }
	 
	 public void setName(String name){
		 this.name = name;
	 }
	 
	 
	 public void study(){
		 this.name=  "����";
		 System.out.println(name+"�ڌW��");
	 }
	 
	 public void sayHello(String sname){
		 System.out.println(name+"��"+sname+"�f����ã�");
	 }
	
}



