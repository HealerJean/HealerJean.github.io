package myproject01;

import java.io.InputStream;
import java.util.Scanner;

/**
 * 
 * @author �����
 *
 */
/*
 * �����������ļӷ�
 */
public class TextMethod {
	public static void main(String args[]){
		text01();
		int a,b,c;
		Scanner s = new Scanner(System.in); 
		
		System.out.printf("\n");		
		System.out.println("    �����һ������");
		System.out.printf("\t");
		a = s.nextInt();
		
		System.out.printf("\n");		
		System.out.println("    ����ڶ�������");
		System.out.printf("\t");
		b = s.nextInt();
		
		System.out.printf("\n");
		c = max(a,b);
		System.out.printf("\t"+c);
	}
	private static Scanner Scanner(InputStream in) {
		// TODO �Զ����ɵķ������
		return null;
	}
	public static void text01(){
		
		System.out.println("  ������������ֵ�����Ҽ�����ߵĺ� ");
	}
	public static int max(int a,int b){
		int jia = a + b;
		return jia;
	}
}
