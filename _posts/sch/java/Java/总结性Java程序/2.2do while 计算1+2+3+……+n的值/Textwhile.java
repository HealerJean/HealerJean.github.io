package myproject01;

import java.util.Scanner;

/**
 * 
 * @author �����
 * @version ��֪��
 *
 */
/*���� 1+2+����n ��ֵ��*/

public class Textwhile {
		public static void main(String args[]){
			System.out.println("����� 1 + 2 + 3 + ���� +n ��ֵ ");
			System.out.println("       ������������ n ��ֵ");	
			System.out.printf("\n");
				int sum = 0;
			Scanner s = new Scanner(System.in);
			
			int n = s.nextInt(        );
			
			while( n > 0 ){
				sum= sum + n;
				n--;
			}
			System.out.printf("\n");
			System.out.println("����� 1 + 2 + 3 + ���� +n ��ֵΪ ��" + sum);
		}
}
