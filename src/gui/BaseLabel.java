package gui;

import javax.swing.*;
import java.awt.*;

public class BaseLabel extends JLabel {
	public BaseLabel(String text, int fontSize, boolean bold) {
		super(text, SwingConstants.CENTER);
		setFont(new Font("Comic Sans MS", bold ? Font.BOLD : Font.PLAIN, fontSize));
	}
}
