package cn.bjsxt.test.file;

/**
 * ���Եݹ��㷨
 * @author Administrator
 *
 */
public class TestRecursion {
	
	long  test(long n){
		if(n==1){
			return 1;
		}else{
			return n*test(n-1);
		}
	}
	
	public static void main(String[] args) {
		long d1  = System.currentTimeMillis();
		System.out.println(new TestRecursion().test(20));
		long d2  = System.currentTimeMillis();
		System.out.println(d2-d1);

		//5*4*3*2*1
		int i=5;
		int result = 1;
		while(i>1){
			result *= i*(i-1);
			i-=2;
		}		
		System.out.println("����ǣ�"+result+"���i="+i);
		System.out.printf("����ǣ�%d���i=%d���ַ�����%s",result,i,"uuuuu");
		
	}
}
