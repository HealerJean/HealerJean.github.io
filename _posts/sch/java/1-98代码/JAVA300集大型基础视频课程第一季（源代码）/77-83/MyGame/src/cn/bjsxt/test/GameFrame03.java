package cn.bjsxt.test;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ���Դ�����������ˮƽ�ʹ�ֱ�ƶ�
 * @author dell
 *
 */
public class GameFrame03 extends Frame {    //GUI��̣�AWT,swing�ȡ�
	
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
	private boolean left;
	private boolean up;
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, (int)x, (int)y, null);
		
		/*if(left){
			x -= 3;
		}else{
			x += 3;
		}
		if(x>500-30){
			left = true;
		}
		if(x<0){
			left = false;
		}*/
		
		if(up){
			y -= 10;
		}else{
			y +=10;
		}
		if(y>500-30){
			up = true;
		}
		if(y<30){
			up = false;
		}
//		y += 3;
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
		GameFrame03 gf = new GameFrame03();
		gf.launchFrame();
	}
	
}
