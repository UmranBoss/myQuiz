package gui;

import javax.swing.JCheckBox;

public class BaseCheckBox extends JCheckBox implements Constants {

	public BaseCheckBox(String text) {
		super(text);
		setFont(FONT_TITLE);
		setForeground(COLOR_TITLE);
	}

}
