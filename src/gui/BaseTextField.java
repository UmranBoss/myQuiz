package gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * {@code BaseTextField} ist eine benutzerdefinierte
 * {@code JTextField}-Erweiterung, die einen Platzhaltertext unterstützt. Der
 * Platzhaltertext wird angezeigt, wenn das Textfeld leer ist und keinen Fokus
 * hat, und wird entfernt, wenn das Textfeld den Fokus erhält.
 * 
 * @see JTextField
 */
public class BaseTextField extends JTextField implements Constants {

	private String placeholder;
	private boolean isPlaceholder;

	/**
	 * Erstellt ein {@code BaseTextField} mit einem Platzhaltertext. Wenn das
	 * Textfeld den Fokus verliert und leer ist, wird der Platzhaltertext wieder
	 * angezeigt. Beim Fokussieren wird der Platzhaltertext entfernt, um die Eingabe
	 * des Benutzers zu ermöglichen.
	 * 
	 * @param placeholder Der Platzhaltertext, der im Textfeld angezeigt wird, wenn
	 *                    es leer ist und keinen Fokus hat.
	 */
	public BaseTextField(String placeholder) {
		/*
		 * Mit super() wird der grundlegende Zustand des JTextField initialisiert, bevor
		 * man mit spezifischen Anpassungen fortfährt (wie der Festlegung der
		 * bevorzugten Größe).
		 */

		super();
		setPreferredSize(TEXTFIELD_SIZE);
		setMaximumSize(TEXTFIELD_SIZE);
		setMinimumSize(TEXTFIELD_SIZE);
		setText(placeholder);
		setFont(TEXTFIELD_FONT);
		setForeground(TEXTFIELD_COLOR);

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (getText().equals(placeholder)) {
					setText("");
					setForeground(TEXTFIELD_COLOR);
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (getText().isEmpty()) {
					setText(placeholder);
					setForeground(Color.LIGHT_GRAY);
				}

			}
		});
	}

};