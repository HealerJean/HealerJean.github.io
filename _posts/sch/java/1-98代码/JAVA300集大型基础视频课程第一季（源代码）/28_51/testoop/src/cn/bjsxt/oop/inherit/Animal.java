package cn.bjsxt.oop.inherit;

/**
 * ���Լ̳�
 * @author dell
 *
 */
public class Animal /*extends Object*/ {
	String eye;
	
	public void run(){
		System.out.println("���ܣ�");
	}
	public void eat(){
		System.out.println("�Գԣ�");
	}
	public void sleep(){
		System.out.println("zzzzz");
	}
	
	public  Animal(){
		super();
		System.out.println("����һ�����");
	}
	
}

class Mammal extends Animal {
	
	public void taisheng(){
		System.out.println("����̥��");
	}
	
}

class Bird  extends Animal {
	public void run(){
		super.run();
		System.out.println("����һ��СССС�񣬷�ѽ�ɲ���");
	}
	
	public void eggSheng(){
		System.out.println("����");
	}
	
	public Bird(){
		super();
		System.out.println("��һ�������");
	}
	
}

