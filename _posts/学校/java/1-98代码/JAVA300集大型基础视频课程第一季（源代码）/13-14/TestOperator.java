
public class TestOperator {
	
	public static void main(String[] args){
	/*	
		double d = 10.2%3;
		System.out.println(d);
		
		int a = 3;
		int b = a++;   //ִ�����,b=3���ȸ�b��ֵ����������
		int c = ++a;   //ִ�����,c=5��������,�ٸ�b��ֵ
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	*/	
	//	int c = 3/0;
		
	/*	
		boolean c = 1<2&&2>(3/0); 
		System.out.println(c);
	*/
	/*
		//����λ����
		int m = 8;
		int n = 4;
		System.out.println(m&n);
		System.out.println(m|n);
		System.out.println(~m);
		System.out.println(m^n);
		
		
        int a = 3*2*2;
		int b = 3<<3;  //�൱�ڣ�3*2*2;
		int c = 12/2/2;
		int d = 12>>2;
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		
		boolean b1 = true&false;
		System.out.println(b1);
	*/	
	
	/*
		//��չ�����
		int  a = 3;
		a +=5;  //a = a+5;
	*/	
	
	/*
		//�ַ����������Ӻ�����ֻҪ��һ��Ϊ�ַ��������Ϊ�ַ������ӷ����������Ϊ�ַ�����
		System.out.println(4+"5");
	*/	
	
		int a=3;
		int b=5;
		String str= "";
		/*
		if(a<b){
			str = "a<b";
		}else{
			str = "a>b";
		}
		*/
		str = (a<b)?"a<b":"a>=b";
		System.out.println(str);

		
	}

}