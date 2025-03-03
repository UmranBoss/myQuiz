package gui;

import javax.swing.*;

/**
 * {@code BaseButton} ist eine benutzerdefinierte Schaltfläche, die von
 * {@code JButton} erbt. Sie deaktiviert die Fokusmarkierung für ein sauberes
 * Erscheinungsbild.
 * 
 * @see JButton
 */
public class BaseButton extends JButton {
	/**
	 * Erstellt eine {@code BaseButton} Instanz mit dem angegebenen Text und
	 * deaktiviert die Fokusmarkierung.
	 * 
	 * @param text Der Text, der auf der Schaltfläche angezeigt wird.
	 */
	public BaseButton(String text) {
		super(text);
		setFocusPainted(false);
	}
}
