package cn.bjsxt.stringbuilder;

/**
 * ���Կɱ��ַ����С�StringBuilder(�̲߳���ȫ��Ч�ʸ�),StringBuffer(�̰߳�ȫ��Ч�ʵ�)
 * String�����ɱ��ַ�����
 * @author dell
 *
 */
public class Test01 {
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();   //�ַ����鳤�ȳ�ʼΪ16
		StringBuilder sb1 = new StringBuilder(32);   //�ַ����鳤�ȳ�ʼΪ32
		StringBuilder sb2 = new StringBuilder("abcd");   //�ַ����鳤�ȳ�ʼΪ32, value[]={'a','b','c','d',\u0000,\u0000...}
		sb2.append("efg");
		sb2.append(true).append(321).append("���");   //ͨ��return thisʵ�ַ�����.
		
		System.out.println(sb2);
		
		System.out.println("##################");
		
		StringBuilder gh = new StringBuilder("a");
		for (int i = 0; i < 1000; i++) {
			gh.append(i);
		}
		System.out.println(gh);
		
		
		
		
	}
}
