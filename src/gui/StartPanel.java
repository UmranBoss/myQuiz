package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel, das die Startseite der Anwendung darstellt. Es zeigt einen
 * Begrüßungstext und bietet zwei Buttons: Einen zum Erstellen eines neuen
 * Quizzes und einen, um die eigenen Quizzes anzuzeigen.
 * 
 * Das Panel ermöglicht es dem Benutzer, zwischen verschiedenen Ansichten (Tabs)
 * zu wechseln.
 */
public class StartPanel extends BasePanel {
	/**
	 * Konstruktor für das StartPanel.
	 * 
	 * Das Panel zeigt einen Begrüßungstext und zwei Buttons: Einen zum Erstellen
	 * eines neuen Quizzes und einen, um die eigenen Quizzes anzusehen. Durch Klick
	 * auf die Buttons wird der Benutzer zu den jeweiligen Tabs weitergeleitet.
	 * 
	 * @param mainPanel Das Hauptpanel, das die verschiedenen Seiten (mit
	 *                  CardLayout) enthält.
	 * @param baseTab   Das TabbedPane, das die verschiedenen Registerkarten
	 *                  verwaltet.
	 */
	public StartPanel(JPanel mainPanel, BaseTab baseTab) {

		setLayout(new BorderLayout());
		CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

		JLabel titleLabel = new BaseLabel("Hallo und willkommen bei bbQQuiz!", 24, true);
		JLabel descriptionLabel = new BaseLabel(
				"Die einfache Möglichkeit, Quizze zu erstellen & das Lernen Deiner Schüler interaktiv zu gestalten!",
				14, false);

		JPanel buttonPanel = new JPanel();
		JButton createQuizButton = new BaseButton("Quiz erstellen");
		JButton myQuizzesButton = new BaseButton("Meine Quizzes");

		// Button-Listener für "Quiz erstellen" → Öffnet den Tab "Quiz erstellen"
		createQuizButton.addActionListener(e -> {
			baseTab.setSelectedIndex(0); // Tab 1 ("Quiz erstellen")
			cardLayout.show(mainPanel, "QuizSeiten");
		});

		// Button-Listener für "Meine Quizzes" → Öffnet den Tab "Meine Quizzes"
		myQuizzesButton.addActionListener(e -> {
			baseTab.setSelectedIndex(1); // Tab 2 ("Meine Quizzes")
			cardLayout.show(mainPanel, "MeineQuizzes");
		});

		buttonPanel.add(createQuizButton);
		buttonPanel.add(myQuizzesButton);

		add(titleLabel, BorderLayout.NORTH);
		add(descriptionLabel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
