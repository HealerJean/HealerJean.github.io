/**
 * ���Է���
 * 
 * @author dell
 * 
 */
public class TestMethod {
	
	//��Ʒ�����ԭ�򣺷����ı����ǹ��ܿ飬����ʵ��ĳ�����ܵ�����ļ��ϡ�   ������Ʒ�����ʱ����ñ��ַ�����ԭ���ԣ�����һ������ֻ���1�����ܣ������������Ǻ��ڵ���չ��
	public static void test01(int a) {
		int oddSum = 0; // �������������ĺ�
		int evenSum = 0; // �������ż���ĺ�
		for (int i = 0; i <= a; i++) {
			if (i % 2 != 0) {
				oddSum += i;
			} else {
				evenSum += i;
			}

		}
		System.out.println("�����ĺͣ�" + oddSum);
		System.out.println("ż���ĺͣ�" + evenSum);
	}
	
	public static void test02(int a,int b,int c){
		for (int j = 1; j <= a; j++) {
			if (j % b == 0) {
				System.out.print(j + "\t");
			}
			if (j % (b * c) == 0) {
				System.out.println();
			}
		}
	}
	
	public static int add(int a,int b){
		int sum = a+b;
		if(a==3){
			return 0;    //return �������ã��������������С�����ֵ��
		}
		System.out.println("���");
		return sum;
	}
	
	public static void main(String[] args) {
		test01(1000);
		test02(100,6,3);  //1-100֮�䣬���Ա�6������ÿ�����3����
		System.out.println("###########");
		int s = add(3,5);
		System.out.println(s);

	}
}
