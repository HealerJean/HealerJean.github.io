package cn.bjsxt.test;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 测试窗口物体沿着任意角度飞行。
 * 最后，实现一个小的台球游戏。
 * @author dell
 *
 */
public class GameFrame02 extends Frame {    //GUI编程：AWT,swing等。
	
	Image img = GameUtil.getImage("images/sun.jpg");
	
	/**
	 * 加载窗口
	 */
	public void launchFrame(){
		setSize(500, 500);
		setLocation(100, 100);
		setVisible(true);
		
		new PaintThread().start();  //启动重画线程
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}
	
	private double x=100,y=100;
	private double degree=3.14/3;    //[0,2pi]
	private double speed = 10;
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, (int)x, (int)y, null);
		
		if(speed>0){
			speed -=0.05;
		}else{
			speed = 0;
		}
		
		x += speed*Math.cos(degree);
		y += speed*Math.sin(degree);
		
		if(y>500-30||y<30){
			degree = -degree;
		}
		
		if(x<0||x>500-30){
			degree = Math.PI-degree;
		}
		
	}

	/**
	 * 定义一个重画窗口的线程类，是一个内部类
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
		GameFrame02 gf = new GameFrame02();
		gf.launchFrame();
	}
	
}
