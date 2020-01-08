
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Dice extends JButton {
	// data
	private int _nValue;
	private boolean _isRolled;

	// method
	// Constructor
	public Dice() {
		setText("Roll");
		setFont(new Font("Vernada", Font.BOLD, 30));
		setBackground(Color.gray);
		Roll();
		addActionListener(new ButtonListener());
		_isRolled = false;
	}

	public Dice(int v) {
		setText("Roll");
		setFont(new Font("Vernada", Font.BOLD, 30));
		setBackground(Color.gray);
		if (v >= 1 && v <= 6) {
			_nValue = v;
		} else {
			Roll();
		}
	}

	// get / set
	public int getValue() {
		return _nValue;
	}

	public void setValue(int v) {
		if (v >= 1 && v <= 6) {
			_nValue = v;
		}
	}

	public boolean getIsRolled() {
		return _isRolled;
	}

	public void setIsRolled(boolean b) {
		_isRolled = b;
	}

	public int Roll() {
		_nValue = (int) (Math.random() * 6) + 1;
		_isRolled = true;
		return _nValue;
	}

	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Roll();
		}

	}
}
