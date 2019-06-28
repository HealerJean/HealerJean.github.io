package cn.bjsxt.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ��Ϸ����֪ʶ������
 * @author dell
 *
 */
public class GameFrame extends Frame {    //GUI��̣�AWT,swing�ȡ�
	
	Image img = GameUtil.getImage("images/sun.jpg");
	
	/**
	 * ���ش���
	 */
	public void launchFrame(){
		setSize(500, 500);
		setLocation(100, 100);
		setVisible(true);
		
		
		new PaintThread().start();  //�����ػ��߳�
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}
	
	private double x=100,y=100;
	
	@Override
	public void paint(Graphics g) {
		g.drawLine(100, 100, 200, 200);
		g.drawRect(100, 100, 200, 200);
		g.drawOval(100, 100, 200, 200);
		
		Font f =new Font("����",Font.BOLD,100);
		g.setFont(f);
		
		g.drawString("������ѧ�ø���", 200, 200);
		
		g.fillRect(100, 100, 20, 20);
		
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(300, 300, 20, 20);
		g.setColor(c);
		
		g.drawImage(img, (int)x, (int)y, null);
		
		x += 3;
		y += 3;
	}

	/**
	 * ����һ���ػ����ڵ��߳��࣬��һ���ڲ���
	 * @author dell
	 *
	 */
	class PaintThread extends Thread {
		
		public void run(){
			while(true){
				repaint();
				try {
					Thread.sleep(40); //1s = 1000ms
				} catch (InterruptedException e) {
					e.printStackTrace();
				}   
			}
		}
		
	}
	



	public static void main(String[] args) {
		GameFrame gf = new GameFrame();
		gf.launchFrame();
	}
	
}
