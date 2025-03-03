package gui;

import javax.swing.*;
import java.awt.*;

/**
 * {@code BaseLabel} ist eine benutzerdefinierte {@code JLabel}-Erweiterung, die
 * Text mit einer anpassbaren Schriftgröße und Schriftstärke (normal oder fett)
 * anzeigt.
 * 
 * @see JLabel
 */
public class BaseLabel extends JLabel {
	/**
	 * Erstellt eine {@code BaseLabel} Instanz mit dem angegebenen Text,
	 * Schriftgröße und -stärke.
	 * 
	 * @param text     Der anzuzeigende Text.
	 * @param fontSize Die Schriftgröße.
	 * @param bold     Gibt an, ob der Text fett sein soll.
	 */
	public BaseLabel(String text, int fontSize, boolean bold) {
		super(text, SwingConstants.CENTER);
		setFont(new Font("Comic Sans MS", bold ? Font.BOLD : Font.PLAIN, fontSize));
	}
}
