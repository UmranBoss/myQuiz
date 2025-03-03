package gui;

import javax.swing.*;
import java.awt.*;

/**
 * {@code BasePanel} ist eine benutzerdefinierte {@code JPanel}-Erweiterung, die
 * mit einem {@code BorderLayout} und weißem Hintergrund initialisiert wird.
 * 
 * @see JPanel
 */
public class BasePanel extends JPanel {
	/**
	 * Konstruktor, der das Layout auf {@code BorderLayout} setzt und den
	 * Hintergrund auf weiß setzt.
	 */
	public BasePanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
	}
}
