package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import data.dao.FrageDAO;
import data.dao.QuizDAO;
import data.dto.AntwortDTO;
import data.dto.FrageDTO;
import model.Antwort;
import model.Frage;
import model.Fragetyp;
import model.Quiz;

public class CreateQuestionPanel extends JPanel implements Constants {

	private Quiz currentQuiz;
	private BaseTextField questionTextfield;
	private BaseTextField[] answerFields = new BaseTextField[4];
	private BaseSpinner[] scoreFields = new BaseSpinner[4];
	private BaseCheckBox[] rightCheckBoxes = new BaseCheckBox[4];
	private BaseRadioButton questionType1Radio;
	private BaseRadioButton questionType2Radio;
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;
	private BaseFrame frame;

	public CreateQuestionPanel(Quiz quiz, FrageDAO frageDAO, QuizDAO quizDAO, BaseFrame frame) {

		this.currentQuiz = quiz;
		this.frageDAO = frageDAO;
		this.quizDAO = quizDAO;
		this.frame = frame;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(800, 500));
		Border border = BorderFactory.createLineBorder(Color.RED, 5);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(border, " Erstelle Deine Fragen zum Quiz ");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		titledBorder.setTitleFont(FONT_HEADER);
		titledBorder.setTitleColor(COLOR_HEADER);
		// Leeren Abstand und den TitledBorder kombinieren
		Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
		setBorder(new CompoundBorder(emptyBorder, titledBorder)); // Beide Borders kombinieren
		initComponents();
	}

	private void initComponents() {

		// Subpanels, in einer Map gespeichert
		Map<String, JPanel> subPanels = new LinkedHashMap<>();
//		subPanels.put("headerQuestionPanel", new BasePanel());
//		subPanels.put("questionPanel", new BasePanel());
//		subPanels.put("headerQuestionTypePanel", new BasePanel();		
//		subPanels.put("questionTypePanel", new BasePanel());
//		subPanels.put("headerAnswerPanel", new BasePanel());
//		subPanels.put("answerPanel", new BasePanel());
//		subPanels.put("buttonPanel", new BasePanel());
//		subPanels.put("backButtonPanel", new BasePanel());
		// Oder die Subpanels in einem String packen, statt untereinander zu schreiben
		String[] panelNames = { "headerQuestionPanel", "questionPanel", "headerQuestionTypePanel", "questionTypePanel",
				"headerAnswerPanel", "answerPanel", "buttonPanel", "backButtonPanel" };

		Color[] colors = { new Color(254, 216, 67), new Color(254, 216, 67), new Color(151, 222, 61),
				new Color(151, 222, 61), new Color(106, 169, 255), new Color(106, 169, 255), Color.WHITE, Color.WHITE };

//		Das war meine erste Version		
//		BasePanel headerQuestionPanel = new BasePanel();
//		BasePanel questionPanel = new BasePanel();
//		BasePanel headerQuestionTypePanel = new BasePanel();		
//		BasePanel questionTypePanel = new BasePanel();
//		BasePanel headerAnswerPanel = new BasePanel();
//		BasePanel answerPanel = new BasePanel();
//		BasePanel buttonPanel = new BasePanel();
//		BasePanel backButtonPanel = new BasePanel();

		for (int i = 0; i < panelNames.length; i++) {
			JPanel panel = new JPanel();
			panel.setBackground(colors[i]);
			subPanels.put(panelNames[i], panel);
		}

		// Subpanels aufrufen, um diese zu konfigurieren
		headerQuestionPanel(subPanels.get("headerQuestionPanel"));
		questionPanel(subPanels.get("questionPanel"));
		headerQuestionTypePanel(subPanels.get("headerQuestionTypePanel"));
		questionTypePanel(subPanels.get("questionTypePanel"));
		headerAnswerPanel(subPanels.get("headerAnswerPanel"));
		answerPanel(subPanels.get("answerPanel"));
		buttonPanel(subPanels.get("buttonPanel"));
		backButtonPanel(subPanels.get("backButtonPanel"));

		for (JPanel panel : subPanels.values()) {
			add(panel);
		}
	}

	private JPanel headerQuestionPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		BaseLabel headerQuestionLabel = new BaseLabel("Frage: ", 14, true);
		panel.add(headerQuestionLabel);
		return panel;
	}

	private JPanel questionPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0; // Weist das Textfeld an, den gesamten horizontalen Platz zu nutzen
		gbc.gridwidth = 1; // Weist das Textfeld einer einzigen Spalte zu
		gbc.gridx = 0;
		gbc.gridy = 0;
		questionTextfield = new BaseTextField("Hier bitte Deine Frage ausformulieren.");
		questionTextfield.setDocument(new LimitedDocument(130));
		questionTextfield.setToolTipText("Die Anzahl der Zeichen ist auf 130 begrenzt.");
		panel.add(questionTextfield, gbc);
		return panel;
	}

	private JPanel headerQuestionTypePanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		BaseLabel headerQuestionTypeLabel = new BaseLabel("Fragetyp:", 14, true);
		panel.add(headerQuestionTypeLabel);
		return panel;
	}

	private JPanel questionTypePanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		// Erster RadioButton in Spalte 0
		gbc.gridy = 0;

		questionType1Radio = new BaseRadioButton("Einzelwahl");
		questionType1Radio.setBackground(new Color(151, 222, 61));
		questionType1Radio.setOpaque(true);
		questionType1Radio.setToolTipText("Eine Antwort ist richtig.");
		add(questionType1Radio, gbc);
		// Zweiter RadioButton in Spalte 1
		gbc.gridy++;

		questionType2Radio = new BaseRadioButton("Mehrfachwahl");
		questionType2Radio.setBackground(new Color(151, 222, 61));
		questionType2Radio.setOpaque(true);
		questionType2Radio.setToolTipText("Mehrere Antworten sind richtig");
		add(questionType2Radio, gbc);

		ButtonGroup rGroup = new ButtonGroup();
		rGroup.add(questionType1Radio);
		rGroup.add(questionType2Radio);

		panel.add(questionType1Radio);
		panel.add(questionType2Radio);
		return panel;
	}

	private JPanel headerAnswerPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		BaseLabel headerAnswerLabel = new BaseLabel("Antwortmöglichkeiten:", 14, true);
		panel.add(headerAnswerLabel);
		return panel;

	}

	private JPanel answerPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		/*
		 * Gewicht der Spalten & Zeilen: Die Spalte erhält viel Platz, die Zeile nur
		 * wenig.
		 */
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;

		/*
		 * Erste (lange) Version
		 */
//		// Antwort 1
//		gbc.gridy = 0;
//		gbc.gridx = 0;
//		BaseTextField answer1 = new BaseTextField("Erste Antwortmöglichkeit eingeben.");
//		panel.add(answer1, gbc);
//
//		// Antwort 2
//		gbc.gridy++;
//		gbc.gridx = 0;
//		BaseTextField answer2 = new BaseTextField("Zweite Antwortmöglichkeit eingeben.");
//		panel.add(answer2, gbc);
//
//		// Antwort 3
//		gbc.gridy++;
//		gbc.gridx = 0;
//		BaseTextField answer3 = new BaseTextField("Dritte Antwortmöglichkeit eingeben.");
//		panel.add(answer3, gbc);
//
//		// Antwort 4
//		gbc.gridy++;
//		gbc.gridx = 0;
//		BaseTextField answer4 = new BaseTextField("Vierte Antwortmöglichkeit eingeben.");
//		panel.add(answer4, gbc);
//
//		// Antwort 1: Punkte
//		gbc.gridy = 0;
//		gbc.gridx = 1;
//		BaseTextField score1 = new BaseTextField("Punkt(e) für die erste Antwort eingeben.");
//		panel.add(score1, gbc);
//
//		// Antwort 2: Punkte
//		gbc.gridy++;
//		gbc.gridx = 1;
//		BaseTextField score2 = new BaseTextField("Punkt(e) für die zweite Antwort eingeben.");
//		panel.add(score2, gbc);
//
//		// Antwort 3: Punkte
//		gbc.gridy++;
//		gbc.gridx = 1;
//		BaseTextField score3 = new BaseTextField("Punkt(e) für die dritte Antwort eingeben.");
//		panel.add(score3, gbc);
//
//		// Antwort 4: Punkte
//		gbc.gridy++;
//		gbc.gridx = 1;
//		BaseTextField score4 = new BaseTextField("Punkt(e) für die vierte Antwort eingeben.");
//		panel.add(score4, gbc);
//
//		// Antwort 1: Richtigkeit
//		gbc.gridy = 0;
//		gbc.gridx = 2;
//		BaseCheckBox richtigkeit1 = new BaseCheckBox("Richtigkeit");
//		panel.add(richtigkeit1, gbc);
//
//		// Antwort 2: Richtigkeit
//		gbc.gridy++;
//		gbc.gridx = 2;
//		BaseCheckBox richtigkeit2 = new BaseCheckBox("Richtigkeit");
//		panel.add(richtigkeit2, gbc);
//
//		// Antwort 3: Richtigkeit
//		gbc.gridy++;
//		gbc.gridx = 2;
//		BaseCheckBox richtigkeit3 = new BaseCheckBox("Richtigkeit");
//		panel.add(richtigkeit3, gbc);
//
//		// Antwort 4: Richtigkeit
//		gbc.gridy++;
//		gbc.gridx = 2;
//		BaseCheckBox richtigkeit4 = new BaseCheckBox("Richtigkeit");
//		panel.add(richtigkeit4, gbc);

		/*
		 * Optimierte Version
		 */

		String[] answer = { "Erste Antwortmöglichkeit eingeben.", "Zweite Antwortmöglichkeit eingeben.",
				"Dritte Antwortmöglichkeit eingeben.", "Vierte Antwortmöglichkeit eingeben." };

//		String[] score = { "Punkt(e) für die erste Antwort eingeben.", "Punkt(e) für die zweite Antwort eingeben.",
//				"Punkt(e) für die dritte Antwort eingeben.", "Punkt(e) für die vierte Antwort eingeben." };

		for (int i = 0; i < 4; i++) {
			gbc.gridy = i;

			// Antwortfelder
			gbc.gridx = 0;
			answerFields[i] = new BaseTextField(answer[i]);
			answerFields[i].setPreferredSize(new Dimension(550, 25));
			answerFields[i].setDocument(new LimitedDocument(100));
			answerFields[i].setToolTipText("Die Anzahl der Zeichen ist auf 100 begrenzt.");
			panel.add(answerFields[i], gbc);
			// Punktfelder
			gbc.gridx = 1;
			scoreFields[i] = new BaseSpinner(new SpinnerNumberModel(0, -1, 10, 1));
			scoreFields[i].setToolTipText(
					"<html>Jede Antwort kann Punkte erhalten.<br>Negative Punktzahl für falsche<br>Antworten sind möglich.</html>");
			panel.add(scoreFields[i], gbc);
			// Richtigkeit-Checkbox
			gbc.gridx = 2;
			rightCheckBoxes[i] = new BaseCheckBox("Richtigkeit");
			rightCheckBoxes[i].setBackground(new Color(106, 169, 255));
			rightCheckBoxes[i].setOpaque(true); // Stellt sicher, dass der Hintergrund sichtbar ist
			rightCheckBoxes[i].setToolTipText("<html>Setze ein Häkchen, wenn<br>diese Antwort richtig ist.</html>");
			panel.add(rightCheckBoxes[i], gbc);
		}
		return panel;
	}

	private JPanel buttonPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		BaseButton cancelQuestion = new BaseButton("Abbrechen");
		panel.add(cancelQuestion, gbc);
		gbc.gridx++;
		gbc.gridy = 0;
		BaseButton saveQuestion = new BaseButton("Speichern");
		panel.add(saveQuestion, gbc);

		saveQuestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateInputs()) {
					saveQuestion(currentQuiz);
				}

			}

			private void saveQuestion(Quiz quiz) {

				// Daten aus den Eingabefeldern sammeln
				FrageDTO frageDTO = collectQuestionData();
				if (frageDTO.getAntworten().isEmpty()) {
					JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Es muss mindestens eine Antwort geben!");
					return;
				}

				// Gesamtpunktzahl berechnen
				int gesamtPunktzahl = frageDTO.getAntworten().stream().mapToInt(AntwortDTO::getScore).sum();

				// DTO in ein Frage-Objekt umwandeln
				Frage frage = new Frage(frageDTO.getText(),
						frageDTO.isSingleChoice() ? Fragetyp.EINZELWAHL : Fragetyp.MEHRFACHWAHL, gesamtPunktzahl, quiz // Quiz
																														// übergeben
				);

				// Antworten umwandeln & mit der Frage verknüpfen
				List<Antwort> antworten = new ArrayList<>();
				for (AntwortDTO antwortDTO : frageDTO.getAntworten()) {
					Antwort antwort = new Antwort(antwortDTO.getText(), antwortDTO.getScore(), antwortDTO.isCorrect(),
							frage // Verknüpfe die Antwort mit der Frage
					);
					antworten.add(antwort);
				}
				frage.setAntworten(antworten);

				// Frage dem Quiz hinzufügen
				// quiz.getFragenListe().add(frage);

				// Frage speichern
				boolean erfolg = frageDAO.create(frage);

				// Rückmeldung an den Benutzer
				if (erfolg) {
					JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Frage gespeichert!");

					// Falls das QuestionTablePanel noch nicht existiert, erstellen wir es:
					if (frame.getQuestionTablePanel() == null) {
						frame.createQuestionTablePanel(quiz);
					} else {
						// Aktualisiere das Panel, falls es schon existiert:
						frame.getQuestionTablePanel().setQuiz(quiz);
					}

					// Danach die Tabelle neu laden (damit die neue Frage sichtbar wird):
					frame.getQuestionTablePanel().loadTableData();
					frame.getCardLayout().show(frame.getMainPanel(), "FragenTabelle");
				} else {
					JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Fehler beim Speichern der Frage!",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}

			/*
			 * Benutzereingaben sammeln & ein FrageDTO-Objekt erstellen
			 * 
			 * @return Ein FrageDTO-Objekt mit den gesammelten Daten
			 */

			private FrageDTO collectQuestionData() {
				String frageText = questionTextfield.getText().trim();
				boolean isSingleChoice = questionType1Radio.isSelected();

				List<AntwortDTO> antworten = new ArrayList<>();

				for (int i = 0; i < 4; i++) {
					String antwortText = answerFields[i].getText().trim();
					int score = (Integer) scoreFields[i].getValue();
					boolean isCorrect = rightCheckBoxes[i].isSelected();

					antworten.add(new AntwortDTO(antwortText, score, isCorrect));

				}
				return new FrageDTO(frageText, isSingleChoice, antworten);
			}

			/**
			 * * Prüft, ob alle erforderlichen Eingaben für die Frage gültig sind.
			 * 
			 * @return {@code true}, wenn die Eingaben korrekt sind, sonst {@code false}.
			 * 
			 *         Die Validierung umfasst: Frage und Antworten dürfen nicht leer sein.
			 *         Jede Antwort benötigt eine Punktzahl ungleich 0. Einzelwahl:
			 *         Mindestens eine Antwort muss korrekt sein. Mehrfachwahl: Mindestens
			 *         zwei Antworten müssen korrekt sein.
			 * 
			 *         Bei Fehlern wird eine entsprechende Meldung angezeigt.
			 */
			private boolean validateInputs() {
				// Zuerst: Prüfe, ob die Frage eingegeben wurde
				if (questionTextfield.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Deine Frage fehlt!");
					return false;
				}

				// Zuerst alle Antwortfelder und zugehörige Scores validieren
				for (int i = 0; i < 4; i++) {
					if (answerFields[i].getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(CreateQuestionPanel.this,
								"Antwort " + (i + 1) + " darf nicht leer sein!");
						return false;
					}

					int score = (Integer) scoreFields[i].getValue();
					boolean isCorrect = rightCheckBoxes[i].isSelected();

					if (isCorrect) {
						if (score <= 0) {
							JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Für Antwort " + (i + 1)
									+ " ist die Antwort als richtig markiert. Bitte gib einen positiven Punktwert ein.");
							return false;
						}
					} else {
						if (!(score == 0 || score == -1)) {
							JOptionPane.showMessageDialog(CreateQuestionPanel.this, "Für Antwort " + (i + 1)
									+ " ist die Antwort als falsch markiert. Bitte gib entweder 0 oder -1 Punkte ein.");
							return false;
						}
					}
				}

				// Nun: Überprüfe einmalig die Gesamtzahl der als richtig markierten Antworten
				int correctAnswersCount = 0;
				for (BaseCheckBox checkBox : rightCheckBoxes) {
					if (checkBox.isSelected()) {
						correctAnswersCount++;
					}
				}

				// Validierung je nach Fragetyp
				if (questionType1Radio.isSelected()) {
					// Einzelwahl: Mindestens eine Antwort muss richtig sein
					if (correctAnswersCount < 1) {
						JOptionPane.showMessageDialog(CreateQuestionPanel.this,
								"Für den Fragetyp 'Einzelwahl' muss mindestens eine Antwort als richtig markiert sein.");
						return false;
					}
				} else if (questionType2Radio.isSelected()) {
					// Mehrfachwahl: Mindestens zwei Antworten müssen richtig sein
					if (correctAnswersCount < 2) {
						JOptionPane.showMessageDialog(CreateQuestionPanel.this,
								"Für den Fragetyp 'Mehrfachwahl' müssen mindestens zwei Antworten als richtig markiert sein.");
						return false;
					}
				}

				// Alle Prüfungen bestanden
				return true;
			}

		});
		return panel;

	}

	private JPanel backButtonPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		BaseButton backButton = new BaseButton("Zurück");
		backButton.setToolTipText("Zurück zur Startseite.");
		panel.add(backButton, gbc);
		return panel;
	}

	public void setQuiz(Quiz quiz) {
		this.currentQuiz = quiz;
		System.out.println("📌 DEBUG: Quiz im CreateQuestionPanel aktualisiert mit ID: " + quiz.getId());
	}

	public void resetFields() {
		// Frage-Textfeld leeren
		questionTextfield.setText("");

		// Alle Antwortfelder leeren, Spinners zurücksetzen und Checkboxen deaktivieren
		for (int i = 0; i < answerFields.length; i++) {
			answerFields[i].setText("");
			// Setze den Spinner auf den Standardwert (hier 0 oder einen anderen, sinnvollen
			// Wert)
			scoreFields[i].setValue(0);
			rightCheckBoxes[i].setSelected(false);
		}
	}
}