import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Graphic extends JPanel {
	// data
	private Country[] Panels = new Country[16];
	private Player[] Players = new Player[2];
	private Dice d;
	private int randomIndex, winner; // randomIndex는 황금카드 인덱스
	private final int SALARY=10;
	
	private JPanel playerInfo, countryInfo, infoBox;
	private BackImage DiceImage;
	
	private String goldCards[] = {"원하는 곳으로 이동합니다.\n"+"(단, 월급은 지급되지 않습니다.)", //0
									"보너스 100 만원이 지급되었습니다.", 					  //1
									"상대 플레이어에게서 50만원을 빼앗아 왔습니다.", 			  //2
									"상대 플레이어의 건물/땅 하나를 강제 매각합니다.", 		  //3
									"사회에 80만원을 기부합니다.",						  //4
									"본인의 건물/땅 하나를 강제 매각합니다.", 					  //5
									"상대 플레이어가 당신에게서 50만원을 가져갔습니다.", 		  //6
									"은행에 60만원이 환수되었습니다."} ; 					  //7
	//가장 비싼 건물 제거로
	private JLabel lblProperty, lblInHandMoney, lblWhichPlayer;//Player info
	private JLabel lblGroundName, lblGroundHost, lblGroundPrice, lblBuildingPrice, lblFee;//Ground Info
	
	// method
	public Graphic() {
		setPreferredSize(new Dimension(1050, 710));
		setBackground(Color.white);
		setLayout(null);

		PanelInit(Panels);
		for (int i = 0; i < 16; i++) {
			add(Panels[i]);
		}

		Players[0] = new Player();
		Players[1] = new Player();

		d = new Dice();
		d.setBounds(885, 50, 120, 60);
		add(d);
		
		//주사위 + 정보 공간
		infoBox = new JPanel();
		infoBox.setPreferredSize(new Dimension(340, 710));
		infoBox.setBounds(710, 0, 340, 710);
		infoBox.setBackground(Color.white);
		infoBox.setLayout(null);
		infoBox.setBorder(BorderFactory.createLineBorder(Color.black));
		add(infoBox);
		
		//해당 턴의 플레이어 정보 표시
		playerInfo = new JPanel();
		playerInfo.setPreferredSize(new Dimension(310, 270));
		playerInfo.setBounds(10, 150, 310, 270);
		playerInfo.setBackground(Color.white);
		playerInfo.setLayout(null);
		playerInfo.setBorder(BorderFactory.createTitledBorder("PLAYER"));
		infoBox.add(playerInfo);
		
		lblWhichPlayer = new JLabel("Player 1");
		lblWhichPlayer.setBounds(10, 10, 290, 30);
		lblWhichPlayer.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblWhichPlayer.setVisible(false);
		playerInfo.add(lblWhichPlayer);
		
		lblProperty = new JLabel("Property : ");
		lblProperty.setBounds(10, 50, 290, 30);
		lblProperty.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblProperty.setVisible(false);
		playerInfo.add(lblProperty);
		
		lblInHandMoney = new JLabel("Money : ");
		lblInHandMoney.setBounds(10, 90, 290, 30);
		lblInHandMoney.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblInHandMoney.setVisible(false);
		playerInfo.add(lblInHandMoney);
		
		
		//땅 정보 표시. (이벤트 , 황금카드일 경우는 대화상자로 대체)
		countryInfo = new JPanel();
		countryInfo.setPreferredSize(new Dimension(340, 710));
		countryInfo.setBounds(10, 430, 310, 270);
		countryInfo.setBackground(Color.white);
		countryInfo.setLayout(null);
		countryInfo.setBorder(BorderFactory.createTitledBorder("GROUND"));
		infoBox.add(countryInfo);
		
		lblGroundName = new JLabel("Ground Name : ");
		lblGroundName.setBounds(10, 10, 290, 30);
		lblGroundName.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblGroundName.setVisible(false);
		countryInfo.add(lblGroundName);
		
		lblGroundHost = new JLabel("Who's Country ");
		lblGroundHost.setBounds(10, 50, 290, 30);
		lblGroundHost.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblGroundHost.setVisible(false);
		countryInfo.add(lblGroundHost);
		
		
		lblGroundPrice = new JLabel("Ground Price: ");
		lblGroundPrice.setBounds(10, 100, 290, 30);
		lblGroundPrice.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblGroundPrice.setVisible(false);
		countryInfo.add(lblGroundPrice);
		
		lblBuildingPrice = new JLabel("Building Price : ");
		lblBuildingPrice.setBounds(10, 140, 290, 30);
		lblBuildingPrice.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblBuildingPrice.setVisible(false);
		countryInfo.add(lblBuildingPrice);		
		
		lblFee = new JLabel("Fee : ");
		lblFee.setBounds(10, 180, 290, 30);
		lblFee.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblFee.setVisible(false);
		countryInfo.add(lblFee);
		
		//주사위 던져서 나온 수 이미지
		DiceImage = new BackImage(3,1);
		DiceImage.setBounds(25, 20, 130, 100);
		DiceImage.setBackground(Color.black);
		DiceImage.setVisible(false);
		infoBox.add(DiceImage);
		
	}// constructor
	
	public void Disposal(int cnt, int fee) {
		int index = 11; // 비싼 땅부터 시작 
		
		for(int i=0;i<16;i++)
		{
			int ruSell;//Are you to sell?
			
			if(Panels[index].getValue()>0)//땅이고
			{
				if(Panels[index].getHost() == cnt+1)//현재 사용자와 같은 소유주이고
				{
					ruSell = JOptionPane.showConfirmDialog(this, Panels[index].getName() + "을(를) 통해 매각을 진행하시겠습니까?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
					
					
					if(ruSell == JOptionPane.YES_OPTION)//팔겠다고 할경우
					{
						for(int j=2;j>=0;j--) {
							int price = Panels[index].getBuildingPrice(j+1);
							if (Players[cnt].buildingDisposal(index, j, price)) {
								Panels[index].setBuildingfalse(j);//빌딩 초기화
							}
							if ( Players[cnt].getInHandMoney() >= fee ) break;
						} // 그 땅에서 비싼 건물 순서로 매각 진행
						if ( Players[cnt].getInHandMoney() < fee ) {
							int money = Panels[index].getGroundPrice();
							if (Players[cnt].groundDisposal(index, money)) {
								Panels[index].setHost(0); // 건물 다 팔아도 부족하면 땅도 팜
								setGroundBorder(index, -1);////////////////////////////테두리 없애기
							}
						}
						//Players[cnt].setInHandMoneyPlusSalary(Panels[i].getPrice());//돈 추가
						
					}
				}
			}
			if(Players[cnt].getInHandMoney()>=fee)
			{
				Players[cnt].setInHandMoneyMinus(fee);
				break;
			}
			index--;
			if (index == -1) index = 15;
			
			printInfo(cnt);
		}
	}//Disposal()
	
	public void goldcardDisposal(int cnt)
	{
		int index =11; //비싼땅부터
		int sell = 0;//건물 팔았는지 여부
		for(int i=0;i<16;i++)
		{
			if(Panels[index].getValue()>0)//땅이고
			{
				if(Panels[index].getHost() == cnt+1)//현재 사용자와 같은 소유주이고
				{
					JOptionPane.showMessageDialog(this, Panels[index].getName() + "을(를) 매각합니다.");
					for(int j=2;j>=0;j--) {
						int price = Panels[index].getBuildingPrice(j+1);
						if (Players[cnt].buildingDisposal(index, j, price)) {
							Players[cnt].setInHandMoneyMinus((int)(price*0.3));
							Panels[index].setBuildingfalse(j);//빌딩 초기화
							sell = 1;//건물 팔았으면 1로설정
							if(sell == 1) {
								printInfo(cnt);
								return;
							}
						}
					}
					if(sell == 0) {//빌딩이 안팔렸으면
						int money = Panels[index].getGroundPrice();
						if (Players[cnt].groundDisposal(index, money)) {
							Players[cnt].setInHandMoneyMinus((int)(money*0.3));
							Panels[index].setHost(0);
							setGroundBorder(index, -1);////////////////////////////테두리 없애기
							
							printInfo(cnt);
							return;//땅까지 팔고 리턴
						}
					}
				}
				//JOptionPane.showMessageDialog(this, "현재 소지한 땅이 없습니다");
				
			}
			index--;
			if (index == -1) index = 15;
		}//for
		//모든 땅 확인후 없으면
		JOptionPane.showMessageDialog(this, "현재 소지한 땅이 없습니다");
	}
	
	public void printInfo(int cnt) {
		//player info 출력-------------
		if(cnt == 0) {
			lblWhichPlayer.setText("Player 1");
			lblProperty.setText("Property : " + Players[cnt].getProperty());
			lblInHandMoney.setText("Money : " +  Players[cnt].getInHandMoney());
			
			lblWhichPlayer.setVisible(true);
			lblProperty.setVisible(true);
			lblInHandMoney.setVisible(true);
		}
		
		if(cnt == 1) {
			lblWhichPlayer.setText("Player 2");
			lblProperty.setText("Property : " + Players[cnt].getProperty());
			lblInHandMoney.setText("Money : " +  Players[cnt].getInHandMoney());
			
			lblWhichPlayer.setVisible(true);
			lblProperty.setVisible(true);
			lblInHandMoney.setVisible(true);
		}
		
		if (Panels[Players[cnt].getCurrentPosition()].getValue() > 0) {// 위치가 땅이라면
			lblGroundName.setText("Country Name : " + Panels[Players[cnt].getCurrentPosition()].getName());
			
			if(Panels[Players[cnt].getCurrentPosition()].getHost() == 0)//주인이 없으면
				lblGroundHost.setText("Who's Country : Nothing");
			else
				lblGroundHost.setText("Who's Country : Player " + Panels[Players[cnt].getCurrentPosition()].getHost());
			
			lblGroundPrice.setText("Country Price : " + Panels[Players[cnt].getCurrentPosition()].getGroundPrice());
			lblBuildingPrice.setText("Building Price : " + Panels[Players[cnt].getCurrentPosition()].getBuildingPrice(Panels[Players[cnt].getCurrentPosition()].getNextBuildingNumber()));
			lblFee.setText("Fee : " + Panels[Players[cnt].getCurrentPosition()].Fee());
			
			lblGroundName.setVisible(true);
			lblGroundHost.setVisible(true);
			lblGroundPrice.setVisible(true);
			lblBuildingPrice.setVisible(true);
			lblFee.setVisible(true);
		}
		
		else//땅이 아닐땐 출력 숨김
		{
			lblGroundName.setVisible(false);
			lblGroundHost.setVisible(false);
			lblGroundPrice.setVisible(false);
			lblBuildingPrice.setVisible(false);
			lblFee.setVisible(false);
		
		}
	}
	
	public void renewalscreen() {
		this.revalidate();
		this.repaint();
	}
	
	public void showDiceImage() {
		int i, j, k=0;
		DiceImage.setVisible(true);
		File file = new File("Rolling Dice.WAV");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
		for(i=0;i<3;i++) {
			for(j=0;j<6;j++) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (j%2 == 0) k = 0;
				else if (j%2 != 0) k = 5;
				DiceImage.setDiceImage(j+1,k);
				DiceImage.renewalscreen();
			}
		}
		DiceImage.setDiceImage(d.getValue(),0);
		DiceImage.renewalscreen();
	}


	public void game(int starmMoney) {///////////////////////////////////////////////////////////////////////////////////////////////////////////
		int isPurchase, isDisposal,cnt=0; //cnt는 사용자 구분
		int k = 10;
		String options[] = {"China", "Japan", "Korea", "Brazil", "Argentina", "United Kingdom", "Spain", "France", "USA" , "Canada"};//황금카드에서 원하는 곳으로 가는거 일 경우 자리 고르기(땅만 가능)
		int matching[] = {13, 15, 0, 2, 3, 5, 7, 8, 10, 11};//황금카드에서 고른 칸의 실제 자리
		int togo=0;
		
		Players[0].setInHandMoney(starmMoney);//////////////////////////////////////////////////////////////////////////////////////////////////
		Players[1].setInHandMoney(starmMoney);//////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (int i = 0; i < 16; i++) {// 16칸을 모두 돌면서 플레이어의 위치정보를 주고 표시할지말지 결정
			Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
		}
		
		
		while (true) {
			
			while (!d.getIsRolled()) {
				try {
					Thread.sleep(10);
					renewalscreen();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			showDiceImage();
			
			printInfo(cnt);
			
			Players[cnt].setAccumulateDice(d.getValue());
			
			if(Players[cnt].getAccumulateDice()>=16) {
				Players[cnt].setInHandMoneyPlusSalary(SALARY);//시작칸을 지나면 월급 지급
				Players[cnt].setAccumulateDice(-16);
			}
			
			d.setIsRolled(false);
			Players[cnt].setCurrentPosition(d.getValue());// 주사위 눈금만큼 플레이어 위치 이동
			
			printInfo(cnt);
			for (int i = 0; i < 16; i++) {// 16칸을 모두 돌면서 플레이어의 위치정보를 주고 표시할지말지 결정
				Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
			}
			
			renewalscreen();
			
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}

			if (Panels[Players[cnt].getCurrentPosition()].getValue() == -2) {// 땅이 GoldCard
				randomIndex=(int) (Math.random() * 8); //인덱스 0~7 중 하나 선택함
				switch (randomIndex) {
					case 0://원하는 칸으로 이동
						Object in = JOptionPane.showInputDialog(countryInfo,goldCards[0],"황금 카드", JOptionPane.QUESTION_MESSAGE,null, options, options[0]);//갈 수 있는 칸 선택
						while(in==null) {//취소버튼 누르면 반복
							in = JOptionPane.showInputDialog(countryInfo,goldCards[0],"황금 카드", JOptionPane.QUESTION_MESSAGE,null, options, options[0]);//갈 수 있는 칸 선택
							
						}
						
						String input = (String)in;
						for(int i=0;i<16;i++) {
							if (Panels[i].getName() == input) {
								togo = i;
								break;
							}
						}
						Players[cnt].initPosition(togo);
						for (int i = 0; i < 16; i++) {// 16칸을 모두 돌면서 플레이어의 위치정보를 주고 표시할지말지 결정
							Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
						}
						break;
					case 1:
						JOptionPane.showMessageDialog(countryInfo, goldCards[1], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyPlusSalary(100);//100만원 보너스 받음
						break;
					case 2: 
						JOptionPane.showMessageDialog(countryInfo, goldCards[2], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyPlusSalary(50);//상대돈 받음
						Players[(cnt + 1) % 2].setInHandMoneyMinus(50);//상대는 돈 빼앗김
						break;
					case 3://상대방 비싼 건물 제거
						JOptionPane.showMessageDialog(countryInfo, goldCards[3], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						goldcardDisposal((cnt+1)%2);
						break;
					case 4:
						JOptionPane.showMessageDialog(countryInfo, goldCards[4], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyMinus(80);//사회기부
						break;
					case 5://내땅 비싼것 제거
						JOptionPane.showMessageDialog(countryInfo, goldCards[5], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						goldcardDisposal(cnt);
						break;
					case 6:
						JOptionPane.showMessageDialog(countryInfo, goldCards[6], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						Players[(cnt + 1) % 2].setInHandMoneyPlusSalary(50);//상대가 돈 받음
						Players[cnt].setInHandMoneyMinus(50);//본인 돈 빼앗김
						break;
					case 7: 
						JOptionPane.showMessageDialog(countryInfo, goldCards[7], "황금 카드", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyMinus(60);//은행 환수
						break;
				} 
				printInfo(cnt);
			}else if(Panels[Players[cnt].getCurrentPosition()].getValue() == -3){// 땅이 event
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				JOptionPane.showMessageDialog(countryInfo, "시작점으로 이동합니다!", "EVENT", JOptionPane.INFORMATION_MESSAGE, null);
				Players[cnt].setInHandMoneyPlusSalary(SALARY);//월급
				Players[cnt].initPosition(12);
				Players[cnt].setAccumulateDiceZero();
				for (int i = 0; i < 16; i++) {// 16칸을 모두 돌면서 플레이어의 위치정보를 주고 표시할지말지 결정
					Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
				}
				
			}else if (Panels[Players[cnt].getCurrentPosition()].getValue() == -1) { // 땅이 start
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				//월급 지금은 앞에서 이미 했기 때문에 여기서는 아무것도 안해도 됨
				
				printInfo(cnt);
			}
			
			renewalscreen();
			
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			//황금카드나 나와서 원하는 땅으로 가게 되면, 그 땅에 주인이 없을 경우 땅을 살 수 있게 할 것인가??? 안되게 하려면 else if로 붙이면 됨..
			if (Panels[Players[cnt].getCurrentPosition()].getValue() > 0) {// 위치가 땅이라면(황금카드, 이벤트, 스타트 아님)
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				if (Panels[Players[cnt].getCurrentPosition()].getHost() == 0) { // 땅주인이 없다면
					isPurchase = JOptionPane.showConfirmDialog(this, "Player" + (cnt + 1) + ", 땅을 구입하시겠습니까?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
					if (isPurchase == JOptionPane.YES_OPTION) {// 땅 구매하기로 한 경우
						if (Players[cnt].PurchaseGround(Players[cnt].getCurrentPosition(),
								Panels[Players[cnt].getCurrentPosition()].getGroundPrice())) {// 땅을 샀다면
							Panels[Players[cnt].getCurrentPosition()].setHost(cnt + 1); // 땅 주인을 설정
							
							setGroundBorder(Players[cnt].getCurrentPosition(), cnt);//////////////////땅 주인 보이기
							
						}
					}
				} else if (Panels[Players[cnt].getCurrentPosition()].getHost() == cnt + 1) {
					// 자신의 땅인 경우 건물을 세울지를 물어보고 건물 구매
					
					int isBuilding;
					int[] building;
					int nBuildingPrice;
					int index;
					
					building = Panels[Players[cnt].getCurrentPosition()].getBuilding();
					
					if(building[2] == 1)
					{
						
					}
					else
					{	isBuilding = JOptionPane.showConfirmDialog(this, "건물을 구입하시겠습니까?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
				
					if(isBuilding == JOptionPane.YES_OPTION) {
						if(Players[cnt].getInHandMoney()>Panels[Players[cnt].getCurrentPosition()].getBuildingPrice(Panels[Players[cnt].getCurrentPosition()].getNextBuildingNumber())) {
							for(int i=0;i<3;i++)
							{
								if(Panels[Players[cnt].getCurrentPosition()].setBuildingtrue(i)) {
									int price, number;									
									number = i;
									//Players[cnt].setInHandMoneyMinus(nBuildingPrice);									
									Players[cnt].PurchaseBuilding(Players[cnt].getCurrentPosition(), number, Panels[Players[cnt].getCurrentPosition()].getBuildingPrice(number+1));
									break;
								}
							}
						}
						else
							JOptionPane.showMessageDialog(this, "잔액이 부족하여 건물을 지을 수 없습니다.");
						
					}
					}//각 인덱스값을 직접 올릴수 있도록 수정
					
					

				} else {// 남의 땅인 경우 -통행료 지불해야 함, 지불할때 부동산을 팔아서 지불할지 여부를 물어봄.
					int fee;
					
					fee = Panels[Players[cnt].getCurrentPosition()].Fee();
					JOptionPane.showMessageDialog(this, "상대방에게 " + fee + "만원 을 지불합니다.");
					
					
					if(Players[cnt].getInHandMoney()>fee)
					{
						Players[cnt].setInHandMoneyMinus(fee);
						
					}
					else
					{
						Disposal(cnt,fee);	// 매각
						if (Players[cnt].getAllMoney()<fee) {
							Players[cnt].setProperty(-1);
							Players[cnt].setInHandMoney(-1);
						}
					}
					
					if(cnt == 1)
					{
						Players[0].setInHandMoneyPlusSalary(fee);
					}
					else
						Players[1].setInHandMoneyPlusSalary(fee);
					
				//	
				}
			}
			
			renewalscreen();
			
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if (Players[cnt].getInHandMoney() < 0) Disposal(cnt,0); // 돈이 마이너스가 된 경우 매각을통해 소지금을 플러스로 만듬
			if (Players[(cnt+1)%2].getInHandMoney() < 0) Disposal((cnt+1)%2,0); // 돈이 마이너스가 된 경우 매각을통해 소지금을 플러스로 만듬
			
			// 재산과 소지금이 모두 없어지면 패배 ( 패배조건 1 )
			if (Players[cnt].getAllMoney() < 0) {
				Players[cnt].setIsBankruptcy(true);
			}
			else if (Players[(cnt+1)%2].getAllMoney() < 0) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			
			// 재산과 소지금의 합이 초기자본의 2배가 되면 승리 ( 승리조건 )
			if (Players[cnt].getAllMoney() >= (int)starmMoney*2.5) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			else if (Players[(cnt+1)%2].getAllMoney() >= starmMoney*2) {
				Players[cnt].setIsBankruptcy(true);
			}
			
			/*
			// 한쪽의 재산과 소지금의 합이 4배가 되면 승리 ( 승리조건 )
			if (Players[cnt].getAllMoney() >= Players[(cnt+1)%2].getAllMoney()*4) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			else if (Players[cnt].getAllMoney()*4 <= Players[(cnt+1)%2].getAllMoney()) {
				Players[cnt].setIsBankruptcy(true);
			}
			*/
			
			if (Players[cnt].getIsBankruptcy()) {
				//return cnt;
				//파산 시 게임 결과 표시 및 종료
				winner=(cnt+1)%2;
				break;
			}
			if (Players[(cnt+1)%2].getIsBankruptcy()) {
				winner= cnt;
				break;
			}
			
			printInfo(cnt);
			cnt = (cnt + 1) % 2; // 0~1, turn을 넘김
			
		}
	}

	private void PanelInit(Country[] arr) {
		arr[0] = new Country("Korea", 1, 0);
		arr[1] = new Country("GoldCard", -2, 1);
		arr[2] = new Country("Brazil", 2, 2);
		arr[3] = new Country("Argentina", 2, 3);
		arr[4] = new Country("Event", -3, 4);
		arr[5] = new Country("United Kingdom", 3, 5);
		arr[6] = new Country("GoldCard", -2, 6);
		arr[7] = new Country("Spain", 3, 7);
		arr[8] = new Country("France", 3, 8);
		arr[9] = new Country("GoldCard", -2, 9);
		arr[10] = new Country("USA", 4, 10);
		arr[11] = new Country("Canada", 4, 11);
		arr[12] = new Country("Start", -1, 12); // start
		arr[13] = new Country("China", 1, 13);
		arr[14] = new Country("GoldCard", -2, 14);
		arr[15] = new Country("Japan", 1, 15);
		for (int i = 0; i < 16; i++) {
			CalAndsetPosition(arr[i]);
		}
		
	}

	private void CalAndsetPosition(Country panel) {
		int row = 0, col = -1;
		int inc = 1;
		int i, j, n, cnt;
		int x, y;

		x = y = 0;
		n = 5;
		cnt = -1;

		while (n > 0 && cnt != panel.getNumber()) {
			for (i = 0; i < n && cnt != panel.getNumber(); i++) {
				col += inc;
				cnt++;
			}
			n--;
			if (n == 0 || cnt == panel.getNumber())
				break;
			for (i = 0; i < n && cnt != panel.getNumber(); i++) {
				row += inc;
				cnt++;
			}
			inc *= -1;
		}

		x = x + (140 * col); // 깔끔하게 표현하려면 배경패널사이즈 -10 정도로 띄워주면 됨.
		y = y + (140 * row);

		panel.setBounds(x, y, 150, 150);

	}
	
	public int getWinner() {
		return winner;
	}
	
	public void setGroundBorder(int position, int player) { //땅 사면 테두리 표시
		Color tmp;
		if(player == 0) tmp = Color.blue;
		else if(player == 1) tmp = Color.red;
		else tmp = Color.white;
		
		
		if(position >=13&&position<=15)Panels[position].setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, tmp));
		else if(position >=9 &&position <= 12) Panels[position].setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, tmp));
		else if(position >=5 &&position <= 7)Panels[position].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, tmp));
		else if(position >=1 &&position <= 3)Panels[position].setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, tmp));
		
		if(position == 0) Panels[position].setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, tmp));
		if(position == 8) Panels[position].setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, tmp));
	}
	

}
