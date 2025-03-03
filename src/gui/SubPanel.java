package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Ein allgemeines Subpanel, das als Basis für verschiedene Unterseiten
 * innerhalb der Anwendung dient.
 * 
 * Das Panel hat einen weißen Hintergrund und einen leeren Rand, der rundum 10
 * Pixel Abstand lässt. Dieses Panel wird für spezifische Unterseiten wie
 * beispielsweise die "FrageSeite" verwendet.
 */
public class SubPanel extends JPanel {

	/**
	 * Konstruktor für das SubPanel.
	 * 
	 * Der Konstruktor setzt den Hintergrund des Panels auf Weiß und fügt einen
	 * leeren Rand hinzu, der rundum 10 Pixel Abstand lässt.
	 */
	public SubPanel() {
		super();
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

}
