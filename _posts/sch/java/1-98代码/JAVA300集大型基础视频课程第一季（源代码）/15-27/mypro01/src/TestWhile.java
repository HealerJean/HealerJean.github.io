
/**
 * ����whileѭ�����÷�
 * @author dell
 *
 */
public class TestWhile {
	public static void main(String[] args) {
		
		int a = 1;    //��ʼ��
		while(a<=100){  //�����ж�
			System.out.println(a);    //ѭ����
			a++;  //����
		}
		System.out.println("whileѭ��������");
		
		//���㣺1+2+3+...+100
		int b = 1;
		int sum = 0;
		while(b<=100){
			sum += b;  //sum = sum + b;
			b++;
		}
		System.out.println("��Ϊ��"+sum); 
		
	}
}
