package gui;

import javax.swing.JRadioButton;

public class BaseRadioButton extends JRadioButton implements Constants {

	public BaseRadioButton(String text) {
		super(text);
		setHorizontalTextPosition(RIGHT);
		setFont(FONT_TITLE);
		setForeground(COLOR_TITLE);
		setSelected(true);
	}

}
