package cn.bjsxt.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ����������
 * @author dell
 *
 */
public class TestCalendar {
	public static void main(String[] args) {
		Calendar  c = new GregorianCalendar();
		c.set(2001, Calendar.FEBRUARY, 10, 12, 23, 34);

		//		c.set(Calendar.YEAR, 2001);
//		c.set(Calendar.MONTH, 1);   //����
//		c.set(Calendar.DATE, 10);
		
//		c.setTime(new Date());
		
		Date d = c.getTime();
		System.out.println(d);
		System.out.println(c.get(Calendar.YEAR)); 
		
		//�������ڼ���
		c.add(Calendar.MONTH, -30);
		System.out.println(c);
	}
}
