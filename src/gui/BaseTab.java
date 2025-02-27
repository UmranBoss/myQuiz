package gui;

import javax.swing.*;

public class BaseTab extends JTabbedPane {
	
	private BaseFrame frame;
	
	public BaseTab(BaseFrame frame) {
		this.frame = frame;
		addTab("Quiz erstellen", new CreateQuizPanel(frame));
		addTab("Meine Quizzes", new JPanel().add(new JLabel("Hier ist noch leer...   :-(")));
	}
}
