import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackImage extends JPanel { 
	
	ImageIcon icon;

	
	public BackImage(int type, int number) {
		if (type == 0 ) icon = new ImageIcon(Integer.toString(number+1)+".png"); // 0타입 = 국기이미지
		else if (type == 1) icon = new ImageIcon("Player"+Integer.toString(number)+".png"); // 1타입 = 마커 ( 플레이어 )
		else if (type == 2) icon = new ImageIcon("Building"+Integer.toString(number)+".png"); // 2타입 = 마커 ( 건물 )
		else if (type == 3) icon = new ImageIcon("Dice"+Integer.toString(number+1)+".0"+".png"); // 3타입 = 주사위
	}
	
	public void setDiceImage(int number, int subnum) {
		icon = new ImageIcon("Dice"+Integer.toString(number)+"."+Integer.toString(subnum)+".png");
	} // 게임을 진행하면서 주사위 이미지를 고쳐줄때 이용
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(0,0,0,0));
		g.drawImage(icon.getImage(),0,0,this.getWidth(),this.getHeight(),this);
	} // 주어진 패널에 사진을 그리는 메소드
	
	public void renewalscreen() {
		this.revalidate();
		this.repaint();
	} // 화면 갱신
}
