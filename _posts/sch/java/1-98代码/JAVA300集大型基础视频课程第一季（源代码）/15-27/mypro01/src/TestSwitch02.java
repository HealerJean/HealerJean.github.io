
/**
 * ����JDK7�е�switch������
 * @author dell
 *
 */
public class TestSwitch02 {
	public static void main(String[] args) {
		String  a = "����";
		
		switch (a) {   //JDK7�������ԣ����ʽ����������ַ���������
		case "��ʿ��":
			System.out.println("�������ʿ��");
			break;
		case "����":
			System.out.println("����ĸ���");
			break;
		default:
			System.out.println("��Һã�");
			break;
		}
		
	}
}
