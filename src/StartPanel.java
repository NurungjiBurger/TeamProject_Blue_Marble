import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel{
	private JPanel start, select;
	private JLabel lblStart, lblSelect;
	private JButton btnStart, btnMoney1, btnMoney2;
	private int _startMoney;
	private boolean _isClicked;

	
	public StartPanel( ) {
		start = new JPanel();
		start.setPreferredSize(new Dimension(1050, 710));
		start.setBackground(Color.white);
		start.setLayout(null);
		this.add(start);
		
		lblStart = new JLabel("부루마블 게임을 시작하시겠습니까?");
		lblStart.setFont(new Font("Vernada", Font.BOLD, 50));
		lblStart.setHorizontalAlignment(SwingConstants.CENTER);
		lblStart.setVerticalAlignment(SwingConstants.CENTER);
		lblStart.setBounds(0, 200, 1050, 50);
		start.add(lblStart);
		
		btnStart = new JButton("START");
		btnStart.setPreferredSize(new Dimension(150, 70));
		btnStart.setBounds(450, 350, 150, 70);
		btnStart.setFont(new Font("Vernada", Font.BOLD, 30));
		btnStart.setBackground(Color.black);
		btnStart.setForeground(Color.white);
		btnStart.addActionListener(new StartListener());
		start.add(btnStart);
		
		select = new JPanel();
		select.setPreferredSize(new Dimension(1050, 710));
		select.setBackground(Color.white);
		select.setLayout(null);
		select.setVisible(false);
		this.add(select);
		
		lblSelect = new JLabel("초기 소지금을 선택하세요");
		lblSelect.setFont(new Font("Vernada", Font.BOLD, 50));
		lblSelect.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelect.setVerticalAlignment(SwingConstants.CENTER);
		lblSelect.setBounds(0, 200, 1050, 50);
		select.add(lblSelect);
		
		btnMoney1 = new JButton("500 만원");
		btnMoney1.setPreferredSize(new Dimension(200, 70));
		btnMoney1.setBounds(275, 350, 200, 70);
		btnMoney1.setFont(new Font("Vernada", Font.BOLD, 30));
		btnMoney1.setBackground(Color.black);
		btnMoney1.setForeground(Color.white);
		btnMoney1.addActionListener(new StartListener());
		select.add(btnMoney1);
		
		btnMoney2 = new JButton("1000 만원");
		btnMoney2.setPreferredSize(new Dimension(200, 70));
		btnMoney2.setBounds(575, 350, 200, 70);
		btnMoney2.setFont(new Font("Vernada", Font.BOLD, 30));
		btnMoney2.setBackground(Color.black);
		btnMoney2.setForeground(Color.white);
		btnMoney2.addActionListener(new StartListener());
		select.add(btnMoney2);
		
		_startMoney = 0;
		_isClicked=false;
	
	}
	
	public int getStartMoney() {
		while (!_isClicked) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _startMoney;
	}
	
	public class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj=e.getSource();
			if(obj==btnStart) {
				start.setVisible(false);
				select.setVisible(true);
			}
			if(obj==btnMoney1) {
				_startMoney=500;
				//select.setVisible(false);
				_isClicked=true;
				
			}
			if(obj==btnMoney2) {
				_startMoney=1000;
				//select.setVisible(false);
				_isClicked=true;
				
			}
		}
	}
	
}

