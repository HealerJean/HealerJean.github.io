package cn.bjsxt.exception.test;


/**
 * 
 * @author Administrator
 *
 */
public class MyException extends Exception {
	
	public MyException(){
		
	}
	
	public MyException(String message){
		super(message);
	}
	
}

class TestMyException{
	void test()throws MyException{
		///
	}
	
	public static void main(String[] args) {
		try {
			new TestMyException().test();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
}
