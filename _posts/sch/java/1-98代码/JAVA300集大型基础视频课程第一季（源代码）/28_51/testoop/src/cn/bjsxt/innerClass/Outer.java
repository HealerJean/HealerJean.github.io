package cn.bjsxt.innerClass;


/**
 * �����ڲ����ʹ��
 * @author dell
 *
 */
public class Outer {
	public static void main(String[] args) {
		Face f = new Face();
		Face.Nose n = f.new Nose();
		n.breath();
		Face.Ear e = new Face.Ear();
		e.listen();
	}
}

class Face {
	int type;
	String shape="������";
	static String color="����";
	
	class Nose {
		
		void breath(){
			System.out.println(shape); 
			System.out.println(Face.this.type);
			System.out.println("������");
		}
	}
	
	static class Ear {
		void listen(){
			System.out.println(color); 
			System.out.println("��������"); 
		}
	}
	
}