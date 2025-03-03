package gui;

import javax.swing.JRadioButton;

/**
 * {@code BaseRadioButton} ist eine benutzerdefinierte
 * {@code JRadioButton}-Erweiterung, die den Text rechts vom Button anzeigt,
 * eine benutzerdefinierte Schriftart und Farbe verwendet und standardmäßig
 * ausgewählt ist.
 * 
 * @see JRadioButton
 * @see Constants
 */
public class BaseRadioButton extends JRadioButton implements Constants {
	/**
	 * Erstellt eine {@code BaseRadioButton} Instanz mit dem angegebenen Text. Setzt
	 * die Textposition, Schriftart, Textfarbe und macht den Button standardmäßig
	 * ausgewählt.
	 * 
	 * @param text Der anzuzeigende Text.
	 */
	public BaseRadioButton(String text) {
		super(text);
		setHorizontalTextPosition(RIGHT);
		setFont(FONT_TITLE);
		setForeground(COLOR_TITLE);
		setSelected(true);
	}

}
