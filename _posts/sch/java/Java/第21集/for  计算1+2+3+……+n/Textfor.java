package myproject01;

import java.util.Scanner;

public class Textfor {
	public static void main(String args[]){
		System.out.println("����� 1 + 2 + 3 + ���� +n ��ֵ ");
		System.out.println("       ������������ n ��ֵ");	
		System.out.printf("\n");
		
		Scanner s = new Scanner(System.in);
		int sum = 0,n=s.nextInt();
		for(;n > 0;n--){
			sum = sum + n;
		}
	System.out.println("     ����� 1 + 2 + 3 + ���� +n ��ֵΪ��" + sum);
	}
}
