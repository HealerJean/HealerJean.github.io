

/*
 * ����if���
 */
public class TestIf {
	public static void main(String[] args) {
		double d = Math.random();
		int e =1+(int)(d*6);  
		System.out.println(e);
		
		
		if(e>3) {
			System.out.println("������");
			System.out.println("������!!!");
		}else{
			System.out.println("С����");
		}
			
		
		System.out.println("���Զ�ѡ��ṹ");
		if(e==6){
			System.out.println("�����ǳ��ã�");
		}else if(e>=4){
			System.out.println("��������");
		}else if(e>=2){
			System.out.println("����һ���");
		}else{
			System.out.println("�������ã�");
		}
		
	
	}
}