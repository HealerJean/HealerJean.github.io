package cn.bjsxt.oop.testFinal;


public /*final*/ class Animal {    //final��������˵��������಻�ܱ��̳У�
	
	public /*final*/ void run(){   //final�ӵ�����ǰ�棬��ζ�Ÿ÷������ܱ�������д��
		System.out.println("���ܣ�");
	}

}


class Bird  extends Animal {
	
	public void run(){
		super.run();
		System.out.println("����һ��СССС�񣬷�ѽ�ɲ���");
	}
	
}

