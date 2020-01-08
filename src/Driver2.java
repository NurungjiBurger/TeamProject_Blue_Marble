import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Driver2 {


	public static void main(String[] args) {
		int winner, startMoney;

		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		StartPanel Start = new StartPanel();
		frame.getContentPane().add(Start);
		
		frame.pack();
		frame.setVisible(true);
		
		startMoney = Start.getStartMoney();
		
		frame.remove(Start);
		
		Graphic Board = new Graphic();	
		frame.getContentPane().add(Board);
		
		frame.pack();
		frame.setVisible(true);

		Board.game(startMoney); // 게임 실행
		
		// 게임 종료 후 승자 반환
		winner=Board.getWinner();
		frame.remove(Board); // 보드패널 삭제하고

		frame.setPreferredSize(new Dimension(1050, 710)); // 프레임 크기 조절

	
		// 피니쉬 패널 및 문구
		JPanel Finish = new JPanel();
		Finish.setPreferredSize(new Dimension(1050, 710));
		Finish.setLayout(null);
		Finish.setBackground(Color.white);
		JLabel Cong = new JLabel("Congraturation!");
		Cong.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 50));
		Cong.setHorizontalAlignment(SwingConstants.CENTER);
		Cong.setBounds(0, 200, 1050, 70);
		JLabel User = new JLabel("Player " + (winner + 1) + " Win!!");
		User.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 50));
		User.setHorizontalAlignment(SwingConstants.CENTER);
		User.setBounds(0, 300, 1050, 70);
		Finish.add(Cong);
		Finish.add(User);

		frame.getContentPane().add(Finish);
		frame.pack();
		frame.setVisible(true);

	}
	
	

}// Driver2 class
