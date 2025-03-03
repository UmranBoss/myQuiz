package gui;

import javax.swing.JCheckBox;

/**
 * {@code BaseCheckBox} ist eine benutzerdefinierte {@code JCheckBox}, die mit
 * einer spezifischen Schriftart und Farbe für den Text versehen wird.
 * 
 * @see JCheckBox
 * @see Constants
 */
public class BaseCheckBox extends JCheckBox implements Constants {
	/**
	 * Erstellt eine {@code BaseCheckBox} Instanz mit dem angegebenen Text und setzt
	 * die Schriftart und Textfarbe.
	 * 
	 * @param text Der Text, der in der Checkbox angezeigt wird.
	 */
	public BaseCheckBox(String text) {
		super(text);
		setFont(FONT_TITLE);
		setForeground(COLOR_TITLE);
	}

}
