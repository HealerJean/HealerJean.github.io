package cn.bjsxt.array;

/**
 * ����Ļ�������
 * @author dell
 *
 */
public class Test01 {
	public static void main(String[] args) {
		/**
		 * 1. ��������ͬ��������(�������Ϳ���Ϊ��������)�����򼯺�
		 * 2. ����Ҳ�Ƕ�������Ԫ���൱�ڶ���ĳ�Ա����(��������ڴ�ͼ)
		 * 3. ���鳤�ȵ�ȷ���ģ����ɱ�ġ����Խ�磬�򱨣�ArrayIndexOutofBoundsException
		 */
		int[] a = new int[3];
		a[0] = 23;
		a[1] = 28;
		a[2] = 32;

		Car[] cars = new Car[4];
		cars[0] = new Car("����");
		System.out.println(a.length); 
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
		
	}
}


