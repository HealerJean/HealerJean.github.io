package cn.bjsxt.oop;

public class Test1 {
	 public static void main(String[] args) {
		 //ͨ���������Class Loader����Student�ࡣ ���غ��ڷ������о�����Student�����Ϣ��
		Student s1 = new Student();
		
		s1.name = "����";
		s1.study();
		s1.sayHello("�Rʿ��");
		
		Student s2 = new Student();
		s2.age = 18;
		s2.name="�ϸ�";
		
	}
}
