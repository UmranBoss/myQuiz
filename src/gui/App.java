package gui;

import javax.swing.SwingUtilities;

/**
 * Die {@code App} Klasse dient als Einstiegspunkt für die GUI-Anwendung. Sie
 * enthält die {@code main} Methode, die beim Starten des Programms die
 * Benutzeroberfläche (GUI) initialisiert und anzeigt.
 * 
 * Die GUI wird innerhalb des Event-Dispatch-Threads (EDT) gestartet, indem
 * {@code SwingUtilities.invokeLater()} verwendet wird, um sicherzustellen, dass
 * alle GUI-Komponenten thread-sicher erstellt und angezeigt werden.
 * 
 * @see BaseFrame
 */
public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new BaseFrame().setVisible(true);
		});
	}
}
