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
	private int randomIndex, winner; // randomIndex�� Ȳ��ī�� �ε���
	private final int SALARY=10;
	
	private JPanel playerInfo, countryInfo, infoBox;
	private BackImage DiceImage;
	
	private String goldCards[] = {"���ϴ� ������ �̵��մϴ�.\n"+"(��, ������ ���޵��� �ʽ��ϴ�.)", //0
									"���ʽ� 100 ������ ���޵Ǿ����ϴ�.", 					  //1
									"��� �÷��̾�Լ� 50������ ���Ѿ� �Խ��ϴ�.", 			  //2
									"��� �÷��̾��� �ǹ�/�� �ϳ��� ���� �Ű��մϴ�.", 		  //3
									"��ȸ�� 80������ ����մϴ�.",						  //4
									"������ �ǹ�/�� �ϳ��� ���� �Ű��մϴ�.", 					  //5
									"��� �÷��̾ ��ſ��Լ� 50������ ���������ϴ�.", 		  //6
									"���࿡ 60������ ȯ���Ǿ����ϴ�."} ; 					  //7
	//���� ��� �ǹ� ���ŷ�
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
		
		//�ֻ��� + ���� ����
		infoBox = new JPanel();
		infoBox.setPreferredSize(new Dimension(340, 710));
		infoBox.setBounds(710, 0, 340, 710);
		infoBox.setBackground(Color.white);
		infoBox.setLayout(null);
		infoBox.setBorder(BorderFactory.createLineBorder(Color.black));
		add(infoBox);
		
		//�ش� ���� �÷��̾� ���� ǥ��
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
		
		
		//�� ���� ǥ��. (�̺�Ʈ , Ȳ��ī���� ���� ��ȭ���ڷ� ��ü)
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
		
		//�ֻ��� ������ ���� �� �̹���
		DiceImage = new BackImage(3,1);
		DiceImage.setBounds(25, 20, 130, 100);
		DiceImage.setBackground(Color.black);
		DiceImage.setVisible(false);
		infoBox.add(DiceImage);
		
	}// constructor
	
	public void Disposal(int cnt, int fee) {
		int index = 11; // ��� ������ ���� 
		
		for(int i=0;i<16;i++)
		{
			int ruSell;//Are you to sell?
			
			if(Panels[index].getValue()>0)//���̰�
			{
				if(Panels[index].getHost() == cnt+1)//���� ����ڿ� ���� �������̰�
				{
					ruSell = JOptionPane.showConfirmDialog(this, Panels[index].getName() + "��(��) ���� �Ű��� �����Ͻðڽ��ϱ�?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
					
					
					if(ruSell == JOptionPane.YES_OPTION)//�Ȱڴٰ� �Ұ��
					{
						for(int j=2;j>=0;j--) {
							int price = Panels[index].getBuildingPrice(j+1);
							if (Players[cnt].buildingDisposal(index, j, price)) {
								Panels[index].setBuildingfalse(j);//���� �ʱ�ȭ
							}
							if ( Players[cnt].getInHandMoney() >= fee ) break;
						} // �� ������ ��� �ǹ� ������ �Ű� ����
						if ( Players[cnt].getInHandMoney() < fee ) {
							int money = Panels[index].getGroundPrice();
							if (Players[cnt].groundDisposal(index, money)) {
								Panels[index].setHost(0); // �ǹ� �� �ȾƵ� �����ϸ� ���� ��
								setGroundBorder(index, -1);////////////////////////////�׵θ� ���ֱ�
							}
						}
						//Players[cnt].setInHandMoneyPlusSalary(Panels[i].getPrice());//�� �߰�
						
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
		int index =11; //��Ѷ�����
		int sell = 0;//�ǹ� �ȾҴ��� ����
		for(int i=0;i<16;i++)
		{
			if(Panels[index].getValue()>0)//���̰�
			{
				if(Panels[index].getHost() == cnt+1)//���� ����ڿ� ���� �������̰�
				{
					JOptionPane.showMessageDialog(this, Panels[index].getName() + "��(��) �Ű��մϴ�.");
					for(int j=2;j>=0;j--) {
						int price = Panels[index].getBuildingPrice(j+1);
						if (Players[cnt].buildingDisposal(index, j, price)) {
							Players[cnt].setInHandMoneyMinus((int)(price*0.3));
							Panels[index].setBuildingfalse(j);//���� �ʱ�ȭ
							sell = 1;//�ǹ� �Ⱦ����� 1�μ���
							if(sell == 1) {
								printInfo(cnt);
								return;
							}
						}
					}
					if(sell == 0) {//������ ���ȷ�����
						int money = Panels[index].getGroundPrice();
						if (Players[cnt].groundDisposal(index, money)) {
							Players[cnt].setInHandMoneyMinus((int)(money*0.3));
							Panels[index].setHost(0);
							setGroundBorder(index, -1);////////////////////////////�׵θ� ���ֱ�
							
							printInfo(cnt);
							return;//������ �Ȱ� ����
						}
					}
				}
				//JOptionPane.showMessageDialog(this, "���� ������ ���� �����ϴ�");
				
			}
			index--;
			if (index == -1) index = 15;
		}//for
		//��� �� Ȯ���� ������
		JOptionPane.showMessageDialog(this, "���� ������ ���� �����ϴ�");
	}
	
	public void printInfo(int cnt) {
		//player info ���-------------
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
		
		if (Panels[Players[cnt].getCurrentPosition()].getValue() > 0) {// ��ġ�� ���̶��
			lblGroundName.setText("Country Name : " + Panels[Players[cnt].getCurrentPosition()].getName());
			
			if(Panels[Players[cnt].getCurrentPosition()].getHost() == 0)//������ ������
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
		
		else//���� �ƴҶ� ��� ����
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
		int isPurchase, isDisposal,cnt=0; //cnt�� ����� ����
		int k = 10;
		String options[] = {"China", "Japan", "Korea", "Brazil", "Argentina", "United Kingdom", "Spain", "France", "USA" , "Canada"};//Ȳ��ī�忡�� ���ϴ� ������ ���°� �� ��� �ڸ� ����(���� ����)
		int matching[] = {13, 15, 0, 2, 3, 5, 7, 8, 10, 11};//Ȳ��ī�忡�� �� ĭ�� ���� �ڸ�
		int togo=0;
		
		Players[0].setInHandMoney(starmMoney);//////////////////////////////////////////////////////////////////////////////////////////////////
		Players[1].setInHandMoney(starmMoney);//////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (int i = 0; i < 16; i++) {// 16ĭ�� ��� ���鼭 �÷��̾��� ��ġ������ �ְ� ǥ���������� ����
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
				Players[cnt].setInHandMoneyPlusSalary(SALARY);//����ĭ�� ������ ���� ����
				Players[cnt].setAccumulateDice(-16);
			}
			
			d.setIsRolled(false);
			Players[cnt].setCurrentPosition(d.getValue());// �ֻ��� ���ݸ�ŭ �÷��̾� ��ġ �̵�
			
			printInfo(cnt);
			for (int i = 0; i < 16; i++) {// 16ĭ�� ��� ���鼭 �÷��̾��� ��ġ������ �ְ� ǥ���������� ����
				Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
			}
			
			renewalscreen();
			
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}

			if (Panels[Players[cnt].getCurrentPosition()].getValue() == -2) {// ���� GoldCard
				randomIndex=(int) (Math.random() * 8); //�ε��� 0~7 �� �ϳ� ������
				switch (randomIndex) {
					case 0://���ϴ� ĭ���� �̵�
						Object in = JOptionPane.showInputDialog(countryInfo,goldCards[0],"Ȳ�� ī��", JOptionPane.QUESTION_MESSAGE,null, options, options[0]);//�� �� �ִ� ĭ ����
						while(in==null) {//��ҹ�ư ������ �ݺ�
							in = JOptionPane.showInputDialog(countryInfo,goldCards[0],"Ȳ�� ī��", JOptionPane.QUESTION_MESSAGE,null, options, options[0]);//�� �� �ִ� ĭ ����
							
						}
						
						String input = (String)in;
						for(int i=0;i<16;i++) {
							if (Panels[i].getName() == input) {
								togo = i;
								break;
							}
						}
						Players[cnt].initPosition(togo);
						for (int i = 0; i < 16; i++) {// 16ĭ�� ��� ���鼭 �÷��̾��� ��ġ������ �ְ� ǥ���������� ����
							Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
						}
						break;
					case 1:
						JOptionPane.showMessageDialog(countryInfo, goldCards[1], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyPlusSalary(100);//100���� ���ʽ� ����
						break;
					case 2: 
						JOptionPane.showMessageDialog(countryInfo, goldCards[2], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyPlusSalary(50);//��뵷 ����
						Players[(cnt + 1) % 2].setInHandMoneyMinus(50);//���� �� ���ѱ�
						break;
					case 3://���� ��� �ǹ� ����
						JOptionPane.showMessageDialog(countryInfo, goldCards[3], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						goldcardDisposal((cnt+1)%2);
						break;
					case 4:
						JOptionPane.showMessageDialog(countryInfo, goldCards[4], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyMinus(80);//��ȸ���
						break;
					case 5://���� ��Ѱ� ����
						JOptionPane.showMessageDialog(countryInfo, goldCards[5], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						goldcardDisposal(cnt);
						break;
					case 6:
						JOptionPane.showMessageDialog(countryInfo, goldCards[6], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						Players[(cnt + 1) % 2].setInHandMoneyPlusSalary(50);//��밡 �� ����
						Players[cnt].setInHandMoneyMinus(50);//���� �� ���ѱ�
						break;
					case 7: 
						JOptionPane.showMessageDialog(countryInfo, goldCards[7], "Ȳ�� ī��", JOptionPane.INFORMATION_MESSAGE, null);
						Players[cnt].setInHandMoneyMinus(60);//���� ȯ��
						break;
				} 
				printInfo(cnt);
			}else if(Panels[Players[cnt].getCurrentPosition()].getValue() == -3){// ���� event
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				JOptionPane.showMessageDialog(countryInfo, "���������� �̵��մϴ�!", "EVENT", JOptionPane.INFORMATION_MESSAGE, null);
				Players[cnt].setInHandMoneyPlusSalary(SALARY);//����
				Players[cnt].initPosition(12);
				Players[cnt].setAccumulateDiceZero();
				for (int i = 0; i < 16; i++) {// 16ĭ�� ��� ���鼭 �÷��̾��� ��ġ������ �ְ� ǥ���������� ����
					Panels[i].presentPlayer(0, 1, Players[0].getCurrentPosition(), Players[1].getCurrentPosition());
				}
				
			}else if (Panels[Players[cnt].getCurrentPosition()].getValue() == -1) { // ���� start
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				//���� ������ �տ��� �̹� �߱� ������ ���⼭�� �ƹ��͵� ���ص� ��
				
				printInfo(cnt);
			}
			
			renewalscreen();
			
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			//Ȳ��ī�峪 ���ͼ� ���ϴ� ������ ���� �Ǹ�, �� ���� ������ ���� ��� ���� �� �� �ְ� �� ���ΰ�??? �ȵǰ� �Ϸ��� else if�� ���̸� ��..
			if (Panels[Players[cnt].getCurrentPosition()].getValue() > 0) {// ��ġ�� ���̶��(Ȳ��ī��, �̺�Ʈ, ��ŸƮ �ƴ�)
				for (int i=0;i<16;i++) Panels[i].renewalscreen();
				if (Panels[Players[cnt].getCurrentPosition()].getHost() == 0) { // �������� ���ٸ�
					isPurchase = JOptionPane.showConfirmDialog(this, "Player" + (cnt + 1) + ", ���� �����Ͻðڽ��ϱ�?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
					if (isPurchase == JOptionPane.YES_OPTION) {// �� �����ϱ�� �� ���
						if (Players[cnt].PurchaseGround(Players[cnt].getCurrentPosition(),
								Panels[Players[cnt].getCurrentPosition()].getGroundPrice())) {// ���� ��ٸ�
							Panels[Players[cnt].getCurrentPosition()].setHost(cnt + 1); // �� ������ ����
							
							setGroundBorder(Players[cnt].getCurrentPosition(), cnt);//////////////////�� ���� ���̱�
							
						}
					}
				} else if (Panels[Players[cnt].getCurrentPosition()].getHost() == cnt + 1) {
					// �ڽ��� ���� ��� �ǹ��� �������� ����� �ǹ� ����
					
					int isBuilding;
					int[] building;
					int nBuildingPrice;
					int index;
					
					building = Panels[Players[cnt].getCurrentPosition()].getBuilding();
					
					if(building[2] == 1)
					{
						
					}
					else
					{	isBuilding = JOptionPane.showConfirmDialog(this, "�ǹ��� �����Ͻðڽ��ϱ�?", "BULE MARBLE", JOptionPane.YES_NO_OPTION);
				
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
							JOptionPane.showMessageDialog(this, "�ܾ��� �����Ͽ� �ǹ��� ���� �� �����ϴ�.");
						
					}
					}//�� �ε������� ���� �ø��� �ֵ��� ����
					
					

				} else {// ���� ���� ��� -����� �����ؾ� ��, �����Ҷ� �ε����� �ȾƼ� �������� ���θ� ���.
					int fee;
					
					fee = Panels[Players[cnt].getCurrentPosition()].Fee();
					JOptionPane.showMessageDialog(this, "���濡�� " + fee + "���� �� �����մϴ�.");
					
					
					if(Players[cnt].getInHandMoney()>fee)
					{
						Players[cnt].setInHandMoneyMinus(fee);
						
					}
					else
					{
						Disposal(cnt,fee);	// �Ű�
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
			
			if (Players[cnt].getInHandMoney() < 0) Disposal(cnt,0); // ���� ���̳ʽ��� �� ��� �Ű������� �������� �÷����� ����
			if (Players[(cnt+1)%2].getInHandMoney() < 0) Disposal((cnt+1)%2,0); // ���� ���̳ʽ��� �� ��� �Ű������� �������� �÷����� ����
			
			// ���� �������� ��� �������� �й� ( �й����� 1 )
			if (Players[cnt].getAllMoney() < 0) {
				Players[cnt].setIsBankruptcy(true);
			}
			else if (Players[(cnt+1)%2].getAllMoney() < 0) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			
			// ���� �������� ���� �ʱ��ں��� 2�谡 �Ǹ� �¸� ( �¸����� )
			if (Players[cnt].getAllMoney() >= (int)starmMoney*2.5) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			else if (Players[(cnt+1)%2].getAllMoney() >= starmMoney*2) {
				Players[cnt].setIsBankruptcy(true);
			}
			
			/*
			// ������ ���� �������� ���� 4�谡 �Ǹ� �¸� ( �¸����� )
			if (Players[cnt].getAllMoney() >= Players[(cnt+1)%2].getAllMoney()*4) {
				Players[(cnt+1)%2].setIsBankruptcy(true);
			}
			else if (Players[cnt].getAllMoney()*4 <= Players[(cnt+1)%2].getAllMoney()) {
				Players[cnt].setIsBankruptcy(true);
			}
			*/
			
			if (Players[cnt].getIsBankruptcy()) {
				//return cnt;
				//�Ļ� �� ���� ��� ǥ�� �� ����
				winner=(cnt+1)%2;
				break;
			}
			if (Players[(cnt+1)%2].getIsBankruptcy()) {
				winner= cnt;
				break;
			}
			
			printInfo(cnt);
			cnt = (cnt + 1) % 2; // 0~1, turn�� �ѱ�
			
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

		x = x + (140 * col); // ����ϰ� ǥ���Ϸ��� ����гλ����� -10 ������ ����ָ� ��.
		y = y + (140 * row);

		panel.setBounds(x, y, 150, 150);

	}
	
	public int getWinner() {
		return winner;
	}
	
	public void setGroundBorder(int position, int player) { //�� ��� �׵θ� ǥ��
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
