
/*
 * ����swtich���
 */
public class TestSwitch {
	public static void main(String[] args) {
		double d = Math.random();
		int e =1+(int)(d*6);  
		System.out.println(e);
		
		System.out.println("���Զ�ѡ��ṹ");
		if(e==6){
			System.out.println("�����ǳ��ã�");
		}else if(e==5){
			System.out.println("�����ܲ���");
		}else if(e==4){
			System.out.println("�������а�");
		}else{   //1,2,3
			System.out.println("�������ã�");
		}
		
		System.out.println("#########################");
		switch(e){   //int,�����Զ�����תΪint������(byte,char,short)��ö�١�//JDK7�п��Է����ַ�����
		case 6:
			System.out.println("�����ǳ��ã�");
			break;    //һ����ÿ��case���涼Ҫ��break����ֹ����case��͸����
		case 5:
			System.out.println("�����ܲ���");
			break;
		case 4:
			System.out.println("�������а�");
			break;
		default:
			System.out.println("�������ã�");
			break;
		}
		
		System.out.println("***************************�������ӷ�����������case��͸����");
		char  c = 'a';
		int rand =(int) (26*Math.random());
		char c2 = (char)(c+rand);
		System.out.print(c2 + ": ");
		switch (c2) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			System.out.println("Ԫ��");  
			break;
		case 'y':
		case 'w':
			System.out.println("��Ԫ��");   
			break;
		default:
			System.out.println("����");
		}
		
		
		
	}
}
