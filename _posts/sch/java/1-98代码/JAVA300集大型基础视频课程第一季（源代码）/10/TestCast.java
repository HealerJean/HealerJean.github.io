//�����Զ�ת�ͺ�ǿ��ת�� 
public class TestCast {
	
	public static void main(String[] args){
	/*	
		byte b = 123;
		//byte b2 = 300;
		//char c = -3;
		char c2 = 'a';
		int i = c2;
		long d01 = 123213;
		float f = d01;
		
		//����ǿ��ת��
		int i2 = -100;
		char c3 = (char)i2;   //-100����char�ı�����Χ������ת������ȫ��ͬ��ֵ���������ֵ��
		System.out.println(c3);
	*/	
	/*
		//���ʽ�е�������������
		int a = 3;
		long b = 4;
		double d = 5.3;
		int c = (int)(a+b);   //�����еĶ�Ԫ�����(+-/*%)���������������������⣡
		float f = (float)(a + d);
	*/
	
		int money = 1000000000;  //10��
		int years = 20;
		long total = (long)money*years;   //���ص��Ǹ���
		System.out.println(total);
		
		//һ����70���������ٴ�
		long times = 70L*60*24*365*70;
		System.out.println(times);
		
		
	}

}