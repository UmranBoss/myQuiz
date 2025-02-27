package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import data.dao.FrageDAO;
import data.dao.QuizDAO;
import model.Frage;
import model.Quiz;

public class QuestionTablePanel extends JPanel {

	private Quiz currentQuiz; // Das Quiz, zu dem die Fragen gehören
	private List<Frage> fragenListe; // Liste aller Fragen des Quiz
	private JTable table;
	private DefaultTableModel tableModel;

	private BaseFrame frame; // Referenz auf dein Hauptfenster
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;

	private CreateQuestionPanel createQuestionPanel;

	public QuestionTablePanel(BaseFrame frame, Quiz quiz, FrageDAO frageDAO, QuizDAO quizDAO) {
		this.frame = frame;
		this.currentQuiz = quiz;
		this.frageDAO = frageDAO;
		this.quizDAO = quizDAO;

		// Aktuelle Fragen aus dem Quiz laden
		if (currentQuiz != null) {
			this.fragenListe = currentQuiz.getFragenListe();
			// Falls in deinem Code noch kein Fragen-List-Attribut vorhanden ist,
			// könntest du es aus der DB laden: fragenListe =
			// frageDAO.findAllByQuizId(currentQuiz.getId());
		}

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Wenn currentQuiz null ist, verwende einen Platzhalter
		String quizTitle = (currentQuiz != null && currentQuiz.getTitel() != null) ? currentQuiz.getTitel()
				: "Kein Quiz ausgewählt";

		// Überschrift
		JLabel headerLabel = new JLabel("Fragebogen für das Quiz: \"" + currentQuiz.getTitel() + "\"");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(headerLabel, BorderLayout.NORTH);

		// Tabelle
		String[] columnNames = { "Fragetext", "Vorschau", "Bearbeiten", "Löschen" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			// Alle Zellen nicht editierbar machen
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(30);

		// Daten in die Tabelle laden
		loadTableData();

		// Spaltenbreiten anpassen (optional)
		table.getColumnModel().getColumn(0).setPreferredWidth(200); // Fragetext
		table.getColumnModel().getColumn(1).setPreferredWidth(60); // Vorschau
		table.getColumnModel().getColumn(2).setPreferredWidth(60); // Bearbeiten
		table.getColumnModel().getColumn(3).setPreferredWidth(60); // Löschen

		// Tabelle in ScrollPane packen
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// Button-Leiste am unteren Rand
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JButton addQuestionButton = new JButton("+ weitere Frage");
		JButton saveQuestionnaireButton = new JButton("Fragebogen speichern");

		// ActionListener: weitere Frage
		addQuestionButton.addActionListener(e -> {
			// Vor dem Wechsel alle Felder im CreateQuestionPanel zurücksetzen
			frame.getCreateQuestionPanel().resetFields();
			frame.getCardLayout().show(frame.getMainPanel(), "FrageSeite");
		});

		// ActionListener: Fragebogen speichern
		saveQuestionnaireButton.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Quiz erfolgreich gespeichert!");
			// Aktualisiere das MyQuizzesPanel, indem du die loadTableData()-Methode
			// aufrufst
			frame.getMeineQuizzesPanel().loadTableData();
			frame.getCardLayout().show(frame.getMainPanel(), "MeineQuizzes");

		});

		buttonPanel.add(addQuestionButton);
		buttonPanel.add(saveQuestionnaireButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// Klick in die Tabelle abfangen
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();

				if (row >= 0 && col >= 0) {
					// Bestimme, welche Spalte angeklickt wurde
					if (col == 1) {
						// Spalte "Vorschau"
						showPreviewDialog(fragenListe.get(row));
					} else if (col == 2) {
						// Spalte "Bearbeiten" (derzeit deaktiviert)
						// Du könntest die Zelle abfragen und prüfen, ob sie "grau" oder "deaktiviert"
						// ist.
						// Oder du verzichtest auf die Aktion, wenn wir es vorerst nicht implementieren
						// wollen.
						JOptionPane.showMessageDialog(QuestionTablePanel.this,
								"Bearbeiten ist derzeit nicht verfügbar.");
					} else if (col == 3) {
						// Spalte "Löschen"
						int confirm = JOptionPane.showConfirmDialog(QuestionTablePanel.this,
								"Willst du diese Frage wirklich löschen?", "Frage löschen", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							deleteQuestion(fragenListe.get(row));
						}
					}
				}
			}
		});
	}

	void loadTableData() {
		// Tabelle leeren
		tableModel.setRowCount(0);

		if (fragenListe != null) {
			for (Frage frage : fragenListe) {
				// Du könntest hier z.B. Icons oder Text verwenden
				// Wir verwenden als Beispiel Text in den Zellen für Vorschau, Bearbeiten,
				// Löschen
				// Später könntest du IconRenderer nutzen.
				Object[] rowData = { frage.getFragetext(), // Fragetext
						"🔍", // Vorschau-Symbol
						"✏️ (grau)", // Bearbeiten (deaktiviert)
						"🗑️" // Löschen
				};
				tableModel.addRow(rowData);
			}
		}
	}

	/**
	 * Öffnet ein Dialogfenster, um die Frage (und Antworten) anzuzeigen.
	 */
	private void showPreviewDialog(Frage frage) {
		JDialog previewDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Vorschau",
				Dialog.ModalityType.APPLICATION_MODAL);
		previewDialog.setSize(400, 300);
		previewDialog.setLocationRelativeTo(this);
		previewDialog.setLayout(new BorderLayout());

		// Überschrift
		JLabel titleLabel = new JLabel("Vorschau: " + frage.getFragetext());
		titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		previewDialog.add(titleLabel, BorderLayout.NORTH);

		// Panel für die Antworten
		JPanel answersPanel = new JPanel();
		answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
		answersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Beispiel: abhängig vom Fragetyp -> RadioButtons oder CheckBoxes
		// Hier vereinfachtes Beispiel: wenn EINZELWAHL => JRadioButton, sonst JCheckBox
		// (In deinem Code hast du die Informationen in frage.getFragetyp())
		if (frage.getAntworten() != null) {
			for (int i = 0; i < frage.getAntworten().size(); i++) {
				// Erstelle dynamisch das passende UI-Element
				if (frage.getFragetyp().name().equals("EINZELWAHL")) {
					JRadioButton rb = new JRadioButton(frage.getAntworten().get(i).getAntworttext());
					rb.setEnabled(false); // Nur Anzeige, kein Editieren
					answersPanel.add(rb);
				} else {
					JCheckBox cb = new JCheckBox(frage.getAntworten().get(i).getAntworttext());
					cb.setEnabled(false);
					answersPanel.add(cb);
				}
			}
		}

		previewDialog.add(answersPanel, BorderLayout.CENTER);

		// Schließen-Button
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton closeButton = new JButton("Schließen");
		closeButton.addActionListener(e -> previewDialog.dispose());
		buttonPanel.add(closeButton);
		previewDialog.add(buttonPanel, BorderLayout.SOUTH);

		previewDialog.setVisible(true);
	}

	/**
	 * Löscht die Frage aus der DB und aktualisiert die Tabelle.
	 */
	private void deleteQuestion(Frage frage) {
		boolean success = frageDAO.delete(frage.getId());
		if (success) {
			// Auch aus der lokalen Liste entfernen
			fragenListe.remove(frage);
			// Tabelle neu laden
			loadTableData();
			JOptionPane.showMessageDialog(this, "Frage erfolgreich gelöscht.");
		} else {
			JOptionPane.showMessageDialog(this, "Fehler beim Löschen der Frage!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Quiz setzen und interne Liste aktualisieren
	public void setQuiz(Quiz quiz) {
		this.currentQuiz = quiz;
		if (quiz != null) {
			this.fragenListe = quiz.getFragenListe();
		}

	}
}
