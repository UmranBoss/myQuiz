package gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * {@code BaseSpinner} ist eine benutzerdefinierte {@code JSpinner}-Erweiterung,
 * die mit einem {@code SpinnerNumberModel} initialisiert wird und feste Größen
 * für den Spinner setzt.
 * 
 * @see JSpinner
 * @see SpinnerNumberModel
 * @see Constants
 */
public class BaseSpinner extends JSpinner implements Constants {
	/**
	 * Erstellt eine {@code BaseSpinner} Instanz mit dem angegebenen
	 * {@code SpinnerNumberModel} und setzt die bevorzugte, maximale und minimale
	 * Größe.
	 * 
	 * @param spinnerNumberModel Das Modell, das die Werte des Spinners definiert.
	 */
	public BaseSpinner(SpinnerNumberModel spinnerNumberModel) {
		super(spinnerNumberModel);
		setPreferredSize(SPINNER_SIZE);
		setMaximumSize(SPINNER_SIZE);
		setMinimumSize(SPINNER_SIZE);
	}

}
