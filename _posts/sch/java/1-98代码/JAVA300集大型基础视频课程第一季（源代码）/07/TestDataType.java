//�����������ͣ�byte,short,int,long���Լ�����֮���ת������
public class TestDataType {
	
	public static void main(String[] args){
		int a = 10;
		int a2 = 010;
		int a3 = 0xf;
	//	byte b = 200;
	//	System.out.println(b);
		System.out.println(a);
		System.out.println(a2);
		System.out.println(a3);
		System.out.println(Integer.toBinaryString(a));
		System.out.println(Integer.toOctalString(a));
		System.out.println(Integer.toHexString(a));
		
		int a5 = 10;
		long a6 = 200;
		byte b2 = 100;   //������ݵĴ�Сû�г���byte/short/char�ı�����Χ��������Զ�ת�͡�
		
		long  a7 = 11123213232L;
		long l = 3;
		long l2 = l+3;    //L����
		
	}

}