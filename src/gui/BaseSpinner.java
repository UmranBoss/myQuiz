package gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class BaseSpinner extends JSpinner implements Constants {

	public BaseSpinner(SpinnerNumberModel spinnerNumberModel) {
		super(spinnerNumberModel);
		setPreferredSize(SPINNER_SIZE);
		setMaximumSize(SPINNER_SIZE);
		setMinimumSize(SPINNER_SIZE);
	}

}
