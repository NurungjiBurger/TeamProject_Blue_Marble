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
		if (type == 0 ) icon = new ImageIcon(Integer.toString(number+1)+".png"); // 0Ÿ�� = �����̹���
		else if (type == 1) icon = new ImageIcon("Player"+Integer.toString(number)+".png"); // 1Ÿ�� = ��Ŀ ( �÷��̾� )
		else if (type == 2) icon = new ImageIcon("Building"+Integer.toString(number)+".png"); // 2Ÿ�� = ��Ŀ ( �ǹ� )
		else if (type == 3) icon = new ImageIcon("Dice"+Integer.toString(number+1)+".0"+".png"); // 3Ÿ�� = �ֻ���
	}
	
	public void setDiceImage(int number, int subnum) {
		icon = new ImageIcon("Dice"+Integer.toString(number)+"."+Integer.toString(subnum)+".png");
	} // ������ �����ϸ鼭 �ֻ��� �̹����� �����ٶ� �̿�
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(0,0,0,0));
		g.drawImage(icon.getImage(),0,0,this.getWidth(),this.getHeight(),this);
	} // �־��� �гο� ������ �׸��� �޼ҵ�
	
	public void renewalscreen() {
		this.revalidate();
		this.repaint();
	} // ȭ�� ����
}
