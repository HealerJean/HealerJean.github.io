package ��һ�����ð�;

import java.util.Scanner;

/**
 * ���������ڲ�������API�ļ�(����a + b �ĺ�)
 * @author Lenovo
 * @version 1.8
 */
public class ZhizuoAPI {
	public static void main(String args[]){
		int a,b,c;
		Scanner s = new Scanner(System.in);
		System.out.printf("   ������ a ��ֵ��\t");
		a = s.nextInt();
		System.out.printf("\n");
		
		System.out.printf("   ������ b ��ֵ��\t");
		b = s.nextInt();
		
		
		c = jiafa(a,b);
		System.out.printf("         a + b = ");
		System.out.printf(" "+c);
	}

	/**
	 * ����һ���ӷ��ķ��������߽к�����
	 * @param a  һ������
	 * @param b һ��������
	 * @return  a��b �ĺ�
	 */
	//����Ķ���д�ڷ�����ǰ��
	public static int jiafa(int a,int b){
		int and;
		and = a + b;
		 return and;
	}
}

