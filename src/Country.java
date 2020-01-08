
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

	private JPanel namePanel; // 이름 써 넣는 칸
	private BackImage imagePanel; // 국기 이미지 칸
	private JLabel countryName; // 국가 이름 라벨 ( Start, GoldCard, CountryName )
	private int groundHost; // 땅 주인이 누구인가
	private int[] buildings = new int[3]; // 건물이 존재하는가
	private BackImage Image[] = new BackImage[5]; // 플레이어 이미지와 빌딩 이미지 칸
	private Color color, basic; // 배경 색 설정을 위한 색깔
	private int groundValue; // 땅 종류를 구별하는 가치값 ( 0보다 작으면 start나 event, goldcard 0보다 크면 국가 )
	private int groundNumber; // 땅의 번호
	private int groundPrice;// 순수한 땅 가격

	public Country(String str, int value, int number) { // str : 땅 이름, value:땅 종류, number:땅번호
		this.setPreferredSize(new Dimension(150, 150)); // 메인 기본 패널 설정
		this.setBackground(Color.gray);
		this.setLayout(null);

		basic = new Color(0, 255, 0, 100);
		
		groundValue = value; // 땅 종류 설정
		groundNumber = number; // 땅 번호 설정

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
			color = Color.red; // value = 1, 가장 낮은 1 가치의 땅
		else if (value == 2)
			color = Color.cyan; // value = 2, 2 가치 땅
		else if (value == 3)
			color = Color.gray;// value = 3, 3 가치 땅
		else if (value == 4)
			color = Color.black; // value = 4, 4 가치 땅

		namePanel = new JPanel(); // 이름 패널 기본 설정
		namePanel.setBounds(10, 10, 130, 30);
		namePanel.setBackground(color);
		this.add(namePanel,1,0); // 메인 패널에서 부터 몇번째 위에 붙이는가
		
		imagePanel = new BackImage(0,groundNumber); // 이미지 패널 기본 설정
		imagePanel.setPreferredSize(new Dimension(130,100));
		imagePanel.setBounds(10, 40, 130, 100);
		imagePanel.setLayout(null);
		this.add(imagePanel,1,0); // 메인 패널에서 부터 몇번째 위에 붙이는가

		groundHost = 0; // 땅 주인 설정 초기에는 누구의 땅도 아니므로 0

		countryName = new JLabel(str); // 땅이름 라벨 기본 설정
		countryName.setFont(new Font("Verdana", Font.BOLD, 15));
		countryName.setForeground(Color.white);
		if (value < 0 || color == Color.cyan)
			countryName.setForeground(Color.black);

		countryName.setBounds(0, 0, 80, 20);
		countryName.setHorizontalAlignment(SwingConstants.CENTER);
		namePanel.add(countryName); // 패널추가

		if (value > 0) {
			for (int i = 0; i < 3; i++) {
				buildings[i] = 0; // 건물 존재여부는 [index]에 0과 1로 표시
			}
			groundPrice = 10 * value;// 임시로 땅일 경우에만 가격 설정
		} else {
			groundPrice = 0;// 땅이 아니면 가격 0 ( goldcard, start, event )
		}

		if (value > 0) { // 0보다 큰 땅에는 건물이 들어갈 수 있고, 그렇지 않으면 건물이 못 들어감
			Image[0] = new BackImage(2,1);
			Image[1] = new BackImage(2,2);
			Image[2] = new BackImage(2,3);

			Image[0].setBounds(20, 110, 30, 30);
			Image[1].setBounds(60, 100, 30, 40);
			Image[2].setBounds(100, 90, 30, 50);
			// 건물 위치 설정

			this.add(Image[0],2,0);
			this.add(Image[1],2,0);
			this.add(Image[2],2,0);
			// 메인 패널에 몇번째 위에 있는가

			Image[0].setVisible(false);
			Image[1].setVisible(false);
			Image[2].setVisible(false);
			// 이미지 숨기기
		}
		// 건물 이미지 value가 0보다 큰 살 수 있는 땅만 건물이미지 추가함
		
		
		Image[3] = new BackImage(1,1);
		Image[4] = new BackImage(1,2);
		// 플레이어 이미지

		Image[3].setBounds(20, 45, 40, 40);
		Image[4].setBounds(90, 45, 40, 40);
		if (value < 0) {
			Image[3].setBounds(20, 65, 40, 40);
			Image[4].setBounds(90, 65, 40, 40);
		}
		// 플레이어 위치 설정

		this.add(Image[3],2,0);
		this.add(Image[4],2,0);
		// 메인 패널에 몇번째 위에 있는가
		

		Image[3].setVisible(false);
		Image[4].setVisible(false);
		// 이미지 숨기기

	} // 생성자 단계에서 땅의 이름과 가치를 설정해주어야만 함.
	
	public int Fee() {
		renewalscreen();
		int sum = 0, cnt=0;

		for (int i = 0; i < 3; i++) {
			sum = sum + (buildings[i] * groundValue*10);
			if (buildings[i] == 1) cnt++;
		} // for문을 이용해 건물들*땅가치*10 = 건물을 통한 통행료

		sum = sum + groundPrice; // 땅 통행료 계산
		
		sum = (int)((double)sum * (0.3 + ((double)cnt * 0.3)));
		// 땅만 있는 경우 최종 sum = sum * 0.3
		// 건물이 한개 있는 경우 최종 sum = sum * 0.6 ... ( 건물 1개당 0.3 씩 증가 )
		// 모든 건물이 있으면 최종 sum = sum * 1.2 ... ( 원 가격보다 더 많이 받을 수 있는 유일한 통행료 )
			
		return sum;
	}
	
	public int getNextBuildingNumber() {
		renewalscreen(); // 화면 갱신에 대해서 정확하게 파악하지 못해서 함수마다 갱신 하도록 일단 해놓았습니다.
		int cnt=1;
		for(int i=0;i<3;i++) {
			if (buildings[i] == 1) cnt++;
		}
		return cnt;
	} // 다음에 건설될 수 있는 건물의 번호가 무엇인가

	public String getName() {
		renewalscreen();
		return countryName.getText();
	} // 땅의 이름이 무엇인가

	public int getHost() {
		renewalscreen();
		return groundHost;
	} // 땅의 주인이 누구인가

	public int[] getBuilding() {
		renewalscreen();
		return buildings;
	} // 건물이 존재하는지를 알려주는 빌딩배열 받기

	public int getValue() {
		renewalscreen();
		return groundValue;
	} // 땅 가치가 무엇인가, 그래픽 클래스에서 게임을 돌릴때 땅 가치가 이용됨

	public int getNumber() {
		return groundNumber;
	} // 땅 번호

	public int getGroundPrice() {
		return groundPrice;
	} // 순수한 땅 가격
	
	public int getPrice() {
		renewalscreen();
		int Price = groundPrice;
		for(int i=0;i<3;i++)
			if (buildings[i] == 1)
				Price += (i+1)*10*groundValue; // 땅가격 + 건물가격
		return Price;
	} // 현재 이 땅의 총 가치
	
	public int getBuildingPrice(int index) {
		renewalscreen();
		return (index)*10*groundValue;
	} // 건물의 가격 = 건물번호 * 10 * 땅가치 , 한개의 건물 가격만 알고 싶을 때 사용

	public void setHost(int playernumber) {
		renewalscreen();
		groundHost = playernumber;
	} // 땅주인 설정

	public boolean setBuildingtrue(int index) {	
		if (buildings[index] == 1) {
			renewalscreen();
			return false; // 건물이 이미 있으면 false 리턴
		}
		else {
			buildings[index] = 1;
			Image[index].setVisible(true); // 인덱스번호 == 건물번호 visible true로 교체
			Image[index].renewalscreen();
			renewalscreen();
			return true;
		} // 건물이 없을때에만 건물배열에 1넣고 성공했단 의미로 true 리턴
	} // 건물 세우기

	public boolean setBuildingfalse(int index) {
		if (buildings[index] == 0) {
			renewalscreen();
			return false; // 건물이 없으면 false 리턴
		}
		else {
			buildings[index] = 0;
			Image[index].setVisible(false); // 인덱스번호 == 건물번호 visible false로 교체
			renewalscreen();
			return true; // 건물이 있는 경우 0을 넣어 성공했으니 true 리턴
		}
	} // 건물 없애기
	
	public void presentBuilding() {
		for (int i = 0; i < 3; i++) {
			if (buildings[i] == 1) {
				Image[i].setVisible(true);
				Image[i].renewalscreen();
			}
		}
		renewalscreen();
	} // 현재 세워진 건물들만 화면에 보이도록함

	public void presentPlayer(int player1, int player2, int player1position, int player2position) {
		if (player1position == groundNumber) {
			Image[player1 + 3].setVisible(true);
			Image[player1 + 3].renewalscreen();
			// 플레이어가 현재 땅위에 있는지 체크하고 이미지를 그려줌.
		} else {
			Image[player1 + 3].setVisible(false);
			// 플레이어가 없다면 이미지를 지움.
		}
		if (player2position == groundNumber) {
			Image[player2 + 3].setVisible(true);
			Image[player2 + 3].renewalscreen();
		} else {
			Image[player2 + 3].setVisible(false);
		}
		renewalscreen(); // 화면 갱신
	}
	
	public void renewalscreen() {
		this.revalidate();
		this.repaint();
	} // 화면 갱신을 위한 함수를 모아둠

}
