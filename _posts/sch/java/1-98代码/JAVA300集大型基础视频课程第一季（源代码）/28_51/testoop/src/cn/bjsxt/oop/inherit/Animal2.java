package cn.bjsxt.oop.inherit;

/**
 * �������
 * @author dell
 *
 */
public class Animal2 {
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
	
	public  Animal2(){
		super();
		System.out.println("����һ�����");
	}
	
	public static void main(String[] args) {
		Bird2 b = new Bird2();
		b.run();
		b.animal2.eat();
	}
	
}

class Mammal2  {
	Animal2 animal2=new Animal2();
	public void taisheng(){
		System.out.println("����̥��");
	}
	
}

class Bird2  {
	Animal2 animal2=new Animal2();
	
	public void run(){
		animal2.run();
		System.out.println("����һ��СССС�񣬷�ѽ�ɲ���");
	}
	
	public void eggSheng(){
		System.out.println("����");
	}
	
	public Bird2(){
		super();
		System.out.println("��һ�������");
	}
	
}
