package myproject01;
/**
 * 
 * @author �����
 * @version ��֪��
 *
 */
public class Textswitch {
		public static void main(String args[]){
			double b = Math.random();
			
			int c = (int)(b * 6) +1;
			
			System.out.printf("\n");
			System.out.println(" ɸ����Ϊ 1 2 3 ������ ������ ��Ӯ");
			
			System.out.printf("\n");
			System.out.println(" ɸ����Ϊ 4 5 6 ������ ���͡� ��Ӯ");
			
			System.out.printf("\n");			
			System.out.println("     ɸ����Ϊ�� "+c);	
			
			System.out.printf("\n");
			switch (c){
			
			case       1:
			case	   2:
			case	   3:
			System.out.println("     ��   Ӯ��    �ͷ���������");
				   break;
			default:
			System.out.println("     ��   Ӯ��     ������������");
			break;
			}
		}
}
