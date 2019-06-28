package cn.bjsxt.test;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 测试物体沿着椭圆飞行
 * 最后，实现一个小的台球游戏。
 * @author dell
 *
 */
public class GameFrame05 extends MyFrame { 
	
	Image img = GameUtil.getImage("images/sun.jpg");
	
	private double x=100,y=100;
	private double degree=3.14/3;    //[0,2pi]
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, (int)x, (int)y, null);
		
		x =100+ 100*Math.cos(degree);
		y =200+ 50*Math.sin(degree);
		
		degree +=0.1;
		
	}

	public static void main(String[] args) {
		GameFrame05 gf = new GameFrame05();
		gf.launchFrame();
	}
	
}
