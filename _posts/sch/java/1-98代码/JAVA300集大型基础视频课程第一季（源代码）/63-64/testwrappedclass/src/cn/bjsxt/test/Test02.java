package cn.bjsxt.test;

/**
 * �����Զ�װ��Ͳ���
 * @author dell
 *
 */
public class Test02 {
	public static void main(String[] args) {
//		Integer a = new Integer(1000);
		Integer a = 1000;  //jdk5.0֮�� .  �Զ�װ�䣬�����������ǸĽ����룺Integer a = new Integer(1000);
		
		Integer b = null;
		int c = b;  //�Զ����䣬�������Ľ���b.intValue();
		
		System.out.println(c); 
		
		Integer  d = 1234;
		Integer  d2 = 1234;
		
		System.out.println(d==d2);
		System.out.println(d.equals(d2));
		
		System.out.println("###################"); 
		Integer d3 = -100;    //[-128,127]֮���������Ȼ����������������������
		Integer d4 = -100;
		System.out.println(d3==d4);
		System.out.println(d3.equals(d4));
		
		
	}
}
