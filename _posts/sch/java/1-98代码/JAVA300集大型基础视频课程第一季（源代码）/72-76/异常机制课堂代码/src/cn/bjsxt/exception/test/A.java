package cn.bjsxt.exception.test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
	class A {	
		public void method() throws IOException {	}
	}
	
	class B extends A {		public void method() throws FileNotFoundException {	}
	}
	
	class C extends A {		public void method() {	}
	}
	
//	class D extends A {		public void method() throws Exception {	}     //���������쳣�ķ�Χ���ᱨ��
//	}
	
	class E extends A {		public void method() throws IOException, FileNotFoundException {    }
	}
	class F extends A {		public void method() throws IOException, ArithmeticException {      }
	}
//	class G extends A {		public void method() throws IOException, ParseException {	}
//	}


