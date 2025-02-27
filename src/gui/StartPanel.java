package gui;

import javax.swing.*;

import model.Quiz;

import java.awt.*;
import java.util.ArrayList;

public class StartPanel extends BasePanel {
	public StartPanel(JPanel mainPanel, QuizTabPanel quizTabs) {
		
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
			quizTabs.setSelectedIndex(0); // Tab 1 ("Quiz erstellen")
			cardLayout.show(mainPanel, "QuizSeiten");
		});

		// Button-Listener für "Meine Quizzes" → Öffnet den Tab "Meine Quizzes"
		myQuizzesButton.addActionListener(e -> {
			quizTabs.setSelectedIndex(1); // Tab 2 ("Meine Quizzes")
			cardLayout.show(mainPanel, "MeineQuizzes");
		});

		buttonPanel.add(createQuizButton);
		buttonPanel.add(myQuizzesButton);

		add(titleLabel, BorderLayout.NORTH);
		add(descriptionLabel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
