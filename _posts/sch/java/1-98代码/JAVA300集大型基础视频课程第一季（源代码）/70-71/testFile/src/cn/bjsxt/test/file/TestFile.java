package cn.bjsxt.test.file;

import java.io.File;
import java.io.IOException;

public class TestFile {
	public static void main(String[] args) {
		File f = new File("d:/src3/TestObject.java");
		File f2 = new File("d:/src3");
		File f3 = new File(f2,"TestThis.java");
		File f4 = new File(f2,"TestFile666.java");
		File f5 = new File("d:/src3/aa/bb/cc/ee/ddd");
		f5.mkdirs();
			//f4.createNewFile();
//		f4.delete();
		if(f.isFile()){
			System.out.println("��һ���ļ�");
		}
		if(f2.isDirectory()){
			System.out.println("��һ��Ŀ¼");
		}
		
	}
}
