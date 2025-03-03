package gui;

import javax.swing.*;

import data.dao.QuizDAO;

/**
 * {@code BaseTab} ist eine benutzerdefinierte {@code JTabbedPane}-Erweiterung,
 * die zwei Tabs hinzufügt: Einen für die Erstellung eines Quizzes und einen für
 * die Anzeige der Quizzes.
 * 
 * @see JTabbedPane
 */
public class BaseTab extends JTabbedPane {

	private BaseFrame frame;
	private QuizDAO qD;

	/**
	 * Erstellt eine {@code BaseTab} Instanz, die zwei Tabs hinzufügt: Einen für das
	 * Erstellen eines Quizzes und einen für "Meine Quizzes".
	 * 
	 * @param frame Das {@code BaseFrame}, das die Tabs verwaltet.
	 */
	public BaseTab(BaseFrame frame) {
		this.frame = frame;
		addTab("Quiz erstellen", new CreateQuizPanel(frame));
		addTab("Meine Quizzes", new MyQuizzesPanel(frame, qD));
	}
}
