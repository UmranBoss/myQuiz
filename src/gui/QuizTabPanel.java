package gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import data.dao.QuizDAO;

public class QuizTabPanel extends JTabbedPane {

	private BaseFrame frame;
	private QuizDAO qD;

	public QuizTabPanel(BaseFrame frame) {
		this.frame = frame;
		addTab("Quiz erstellen", new CreateQuizPanel(frame));
		addTab("Meine Quizzes", new MyQuizzesPanel(frame, qD));
	}

}
