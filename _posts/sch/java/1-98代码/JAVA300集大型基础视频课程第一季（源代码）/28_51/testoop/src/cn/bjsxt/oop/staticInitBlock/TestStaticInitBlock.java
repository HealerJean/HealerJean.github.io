package cn.bjsxt.oop.staticInitBlock;

public class TestStaticInitBlock extends Parent001 {
	
	static int a ;
	
	static {
		System.out.println("��̬��ʼ��TestStaticInitBlock!");
		a = 100;
	}
	
	
	public static void main(String[] args) {
		
	}
}
