package myproject01;


/**
 * 
 * @author �����
 * @version 7.0
 *
 */
public class Textif {
	public static void main(String args[]){
		double a = Math.random();  //��ʱһ��[0,1)֮��������

		int b = (int)(a * 6) + 1;  //����һ����ɸ����Ϸ
		
			System.out.printf("\n");
			System.out.println(" ɸ����Ϊ 1 2 3 ������ ������ ��Ӯ");
			
			System.out.printf("\n");
			System.out.println(" ɸ����Ϊ 4 5 6 ������ ���͡� ��Ӯ");
			
			System.out.printf("\n");			
			System.out.println("     ɸ����Ϊ�� "+b);	
			
			System.out.printf("\n");
		if ( b < 4)
		{
	
			System.out.println("     ��   Ӯ��    �ͷ���������");
			
		}
		else{
		
			System.out.println("     ��   Ӯ��������������");			
		}
		


	}

}
