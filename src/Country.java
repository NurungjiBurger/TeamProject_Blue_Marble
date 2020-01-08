
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Country extends JLayeredPane {

	private JPanel namePanel; // �̸� �� �ִ� ĭ
	private BackImage imagePanel; // ���� �̹��� ĭ
	private JLabel countryName; // ���� �̸� �� ( Start, GoldCard, CountryName )
	private int groundHost; // �� ������ �����ΰ�
	private int[] buildings = new int[3]; // �ǹ��� �����ϴ°�
	private BackImage Image[] = new BackImage[5]; // �÷��̾� �̹����� ���� �̹��� ĭ
	private Color color, basic; // ��� �� ������ ���� ����
	private int groundValue; // �� ������ �����ϴ� ��ġ�� ( 0���� ������ start�� event, goldcard 0���� ũ�� ���� )
	private int groundNumber; // ���� ��ȣ
	private int groundPrice;// ������ �� ����

	public Country(String str, int value, int number) { // str : �� �̸�, value:�� ����, number:����ȣ
		this.setPreferredSize(new Dimension(150, 150)); // ���� �⺻ �г� ����
		this.setBackground(Color.gray);
		this.setLayout(null);

		basic = new Color(0, 255, 0, 100);
		
		groundValue = value; // �� ���� ����
		groundNumber = number; // �� ��ȣ ����

		if (value == -2) {
			color = Color.yellow;// goldcard
			basic = Color.yellow;
		} else if (value == -1) {
			color = new Color(255, 175, 175, 100); // Color.pink; // start
			basic = new Color(255, 175, 175, 100);

		} else if (value == -3) {
			color = new Color(255, 175, 175, 100);// event
			basic = new Color(255, 175, 175, 100);
		} else if (value == 1)
			color = Color.red; // value = 1, ���� ���� 1 ��ġ�� ��
		else if (value == 2)
			color = Color.cyan; // value = 2, 2 ��ġ ��
		else if (value == 3)
			color = Color.gray;// value = 3, 3 ��ġ ��
		else if (value == 4)
			color = Color.black; // value = 4, 4 ��ġ ��

		namePanel = new JPanel(); // �̸� �г� �⺻ ����
		namePanel.setBounds(10, 10, 130, 30);
		namePanel.setBackground(color);
		this.add(namePanel,1,0); // ���� �гο��� ���� ���° ���� ���̴°�
		
		imagePanel = new BackImage(0,groundNumber); // �̹��� �г� �⺻ ����
		imagePanel.setPreferredSize(new Dimension(130,100));
		imagePanel.setBounds(10, 40, 130, 100);
		imagePanel.setLayout(null);
		this.add(imagePanel,1,0); // ���� �гο��� ���� ���° ���� ���̴°�

		groundHost = 0; // �� ���� ���� �ʱ⿡�� ������ ���� �ƴϹǷ� 0

		countryName = new JLabel(str); // ���̸� �� �⺻ ����
		countryName.setFont(new Font("Verdana", Font.BOLD, 15));
		countryName.setForeground(Color.white);
		if (value < 0 || color == Color.cyan)
			countryName.setForeground(Color.black);

		countryName.setBounds(0, 0, 80, 20);
		countryName.setHorizontalAlignment(SwingConstants.CENTER);
		namePanel.add(countryName); // �г��߰�

		if (value > 0) {
			for (int i = 0; i < 3; i++) {
				buildings[i] = 0; // �ǹ� ���翩�δ� [index]�� 0�� 1�� ǥ��
			}
			groundPrice = 10 * value;// �ӽ÷� ���� ��쿡�� ���� ����
		} else {
			groundPrice = 0;// ���� �ƴϸ� ���� 0 ( goldcard, start, event )
		}

		if (value > 0) { // 0���� ū ������ �ǹ��� �� �� �ְ�, �׷��� ������ �ǹ��� �� ��
			Image[0] = new BackImage(2,1);
			Image[1] = new BackImage(2,2);
			Image[2] = new BackImage(2,3);

			Image[0].setBounds(20, 110, 30, 30);
			Image[1].setBounds(60, 100, 30, 40);
			Image[2].setBounds(100, 90, 30, 50);
			// �ǹ� ��ġ ����

			this.add(Image[0],2,0);
			this.add(Image[1],2,0);
			this.add(Image[2],2,0);
			// ���� �гο� ���° ���� �ִ°�

			Image[0].setVisible(false);
			Image[1].setVisible(false);
			Image[2].setVisible(false);
			// �̹��� �����
		}
		// �ǹ� �̹��� value�� 0���� ū �� �� �ִ� ���� �ǹ��̹��� �߰���
		
		
		Image[3] = new BackImage(1,1);
		Image[4] = new BackImage(1,2);
		// �÷��̾� �̹���

		Image[3].setBounds(20, 45, 40, 40);
		Image[4].setBounds(90, 45, 40, 40);
		if (value < 0) {
			Image[3].setBounds(20, 65, 40, 40);
			Image[4].setBounds(90, 65, 40, 40);
		}
		// �÷��̾� ��ġ ����

		this.add(Image[3],2,0);
		this.add(Image[4],2,0);
		// ���� �гο� ���° ���� �ִ°�
		

		Image[3].setVisible(false);
		Image[4].setVisible(false);
		// �̹��� �����

	} // ������ �ܰ迡�� ���� �̸��� ��ġ�� �������־�߸� ��.
	
	public int Fee() {
		renewalscreen();
		int sum = 0, cnt=0;

		for (int i = 0; i < 3; i++) {
			sum = sum + (buildings[i] * groundValue*10);
			if (buildings[i] == 1) cnt++;
		} // for���� �̿��� �ǹ���*����ġ*10 = �ǹ��� ���� �����

		sum = sum + groundPrice; // �� ����� ���
		
		sum = (int)((double)sum * (0.3 + ((double)cnt * 0.3)));
		// ���� �ִ� ��� ���� sum = sum * 0.3
		// �ǹ��� �Ѱ� �ִ� ��� ���� sum = sum * 0.6 ... ( �ǹ� 1���� 0.3 �� ���� )
		// ��� �ǹ��� ������ ���� sum = sum * 1.2 ... ( �� ���ݺ��� �� ���� ���� �� �ִ� ������ ����� )
			
		return sum;
	}
	
	public int getNextBuildingNumber() {
		renewalscreen(); // ȭ�� ���ſ� ���ؼ� ��Ȯ�ϰ� �ľ����� ���ؼ� �Լ����� ���� �ϵ��� �ϴ� �س��ҽ��ϴ�.
		int cnt=1;
		for(int i=0;i<3;i++) {
			if (buildings[i] == 1) cnt++;
		}
		return cnt;
	} // ������ �Ǽ��� �� �ִ� �ǹ��� ��ȣ�� �����ΰ�

	public String getName() {
		renewalscreen();
		return countryName.getText();
	} // ���� �̸��� �����ΰ�

	public int getHost() {
		renewalscreen();
		return groundHost;
	} // ���� ������ �����ΰ�

	public int[] getBuilding() {
		renewalscreen();
		return buildings;
	} // �ǹ��� �����ϴ����� �˷��ִ� �����迭 �ޱ�

	public int getValue() {
		renewalscreen();
		return groundValue;
	} // �� ��ġ�� �����ΰ�, �׷��� Ŭ�������� ������ ������ �� ��ġ�� �̿��

	public int getNumber() {
		return groundNumber;
	} // �� ��ȣ

	public int getGroundPrice() {
		return groundPrice;
	} // ������ �� ����
	
	public int getPrice() {
		renewalscreen();
		int Price = groundPrice;
		for(int i=0;i<3;i++)
			if (buildings[i] == 1)
				Price += (i+1)*10*groundValue; // ������ + �ǹ�����
		return Price;
	} // ���� �� ���� �� ��ġ
	
	public int getBuildingPrice(int index) {
		renewalscreen();
		return (index)*10*groundValue;
	} // �ǹ��� ���� = �ǹ���ȣ * 10 * ����ġ , �Ѱ��� �ǹ� ���ݸ� �˰� ���� �� ���

	public void setHost(int playernumber) {
		renewalscreen();
		groundHost = playernumber;
	} // ������ ����

	public boolean setBuildingtrue(int index) {	
		if (buildings[index] == 1) {
			renewalscreen();
			return false; // �ǹ��� �̹� ������ false ����
		}
		else {
			buildings[index] = 1;
			Image[index].setVisible(true); // �ε�����ȣ == �ǹ���ȣ visible true�� ��ü
			Image[index].renewalscreen();
			renewalscreen();
			return true;
		} // �ǹ��� ���������� �ǹ��迭�� 1�ְ� �����ߴ� �ǹ̷� true ����
	} // �ǹ� �����

	public boolean setBuildingfalse(int index) {
		if (buildings[index] == 0) {
			renewalscreen();
			return false; // �ǹ��� ������ false ����
		}
		else {
			buildings[index] = 0;
			Image[index].setVisible(false); // �ε�����ȣ == �ǹ���ȣ visible false�� ��ü
			renewalscreen();
			return true; // �ǹ��� �ִ� ��� 0�� �־� ���������� true ����
		}
	} // �ǹ� ���ֱ�
	
	public void presentBuilding() {
		for (int i = 0; i < 3; i++) {
			if (buildings[i] == 1) {
				Image[i].setVisible(true);
				Image[i].renewalscreen();
			}
		}
		renewalscreen();
	} // ���� ������ �ǹ��鸸 ȭ�鿡 ���̵�����

	public void presentPlayer(int player1, int player2, int player1position, int player2position) {
		if (player1position == groundNumber) {
			Image[player1 + 3].setVisible(true);
			Image[player1 + 3].renewalscreen();
			// �÷��̾ ���� ������ �ִ��� üũ�ϰ� �̹����� �׷���.
		} else {
			Image[player1 + 3].setVisible(false);
			// �÷��̾ ���ٸ� �̹����� ����.
		}
		if (player2position == groundNumber) {
			Image[player2 + 3].setVisible(true);
			Image[player2 + 3].renewalscreen();
		} else {
			Image[player2 + 3].setVisible(false);
		}
		renewalscreen(); // ȭ�� ����
	}
	
	public void renewalscreen() {
		this.revalidate();
		this.repaint();
	} // ȭ�� ������ ���� �Լ��� ��Ƶ�

}
