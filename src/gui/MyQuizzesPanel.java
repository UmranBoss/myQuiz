package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import data.dao.QuizDAO;
import model.Antwort;
import model.Frage;
import model.Quiz;

public class MyQuizzesPanel extends JPanel {

	private JTable table;
	private DefaultTableModel tableModel;
	private QuizDAO quizDAO;
	private BaseFrame frame; // Referenz auf dein Hauptfenster
	private List<Quiz> quizzes; // Merkt sich alle geladenen Quizzes

	public MyQuizzesPanel(BaseFrame frame, QuizDAO quizDAO) {
		this.frame = frame;
		this.quizDAO = quizDAO;
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Überschrift passend zum Mockup
		JLabel headerLabel = new JLabel("Übersicht der erstellten Quizzes");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(headerLabel, BorderLayout.NORTH);

		// Tabellenkopf definieren
		String[] columnNames = { "Quiztitel", "Thema", "Kategorie", "Vorschau", "Bearbeiten", "Löschen",
				"Exportieren" };

		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Keine direkte Bearbeitung in der Tabelle
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(30);

		// Spaltenbreiten (optional anpassen)
		table.getColumnModel().getColumn(0).setPreferredWidth(150); // Quiztitel
		table.getColumnModel().getColumn(1).setPreferredWidth(100); // Thema
		table.getColumnModel().getColumn(2).setPreferredWidth(100); // Kategorie
		table.getColumnModel().getColumn(3).setPreferredWidth(60); // Vorschau
		table.getColumnModel().getColumn(4).setPreferredWidth(60); // Bearbeiten
		table.getColumnModel().getColumn(5).setPreferredWidth(60); // Löschen
		table.getColumnModel().getColumn(6).setPreferredWidth(80); // Exportieren

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// Button-Leiste am unteren Rand
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		JButton refreshButton = new JButton("Aktualisieren");
		refreshButton.addActionListener(e -> loadTableData());

		buttonPanel.add(refreshButton);
		add(buttonPanel, BorderLayout.SOUTH);

		// Klicks in die Tabelle auswerten
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				if (row >= 0 && col >= 0 && quizzes != null && row < quizzes.size()) {

					// Quiz ermitteln
					Quiz selectedQuiz = quizzes.get(row);
					// Hier hättest du ID und könntest zur Sicherheit noch:
					// Quiz fullQuiz = quizDAO.findById(selectedQuiz.getId());

					switch (col) {
					case 3: // Vorschau
						JOptionPane.showMessageDialog(MyQuizzesPanel.this,
								"Vorschau des Quiz: " + selectedQuiz.getTitel() + "\n\n"
										+ "(Hier könntest du ein Dialogfenster öffnen, "
										+ "um Details zum ausgewählten Quiz anzuzeigen.)");
						break;

					case 4: // Bearbeiten (grau)
						JOptionPane.showMessageDialog(MyQuizzesPanel.this, "Bearbeiten ist derzeit nicht verfügbar.");
						break;

					case 5: // Löschen
						int confirm = JOptionPane.showConfirmDialog(MyQuizzesPanel.this,
								"Möchtest du dieses Quiz wirklich löschen?\n" + selectedQuiz.getTitel(), "Quiz löschen",
								JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							// Hier: quizDAO.delete(selectedQuiz.getId());
							// Danach loadTableData() zum Aktualisieren.
							JOptionPane.showMessageDialog(MyQuizzesPanel.this,
									"Quiz '" + selectedQuiz.getTitel() + "' gelöscht (Demo).");
						}
						break;

					case 6: // Exportieren
						exportQuiz(selectedQuiz);
						break;

					default:
						break;
					}
				}
			}
		});
//		loadTableData();
	}

	/**
	 * Lädt alle Quizzes aus der Datenbank und befüllt die Tabelle.
	 */
	public void loadTableData() {
		tableModel.setRowCount(0); // Tabelle leeren
		// Hier alle Quizzes laden
		quizzes = quizDAO.findAll();
		// findAll() muss in deinem QuizDAO implementiert sein.

		if (quizzes != null) {
			for (Quiz quiz : quizzes) {
				Object[] rowData = { quiz.getTitel(), (quiz.getThema() != null) ? quiz.getThema().getBezeichnung() : "",
						(quiz.getKategorie() != null) ? quiz.getKategorie().getBezeichnung() : "", "🔍", // Vorschau
						"✏️ (nicht freigeschaltet)", // Bearbeiten
						"🗑️", // Löschen
						"📤" // Exportieren
				};
				tableModel.addRow(rowData);
			}
		}
	}

	/**
	 * Exportiert das angegebene Quiz als TXT-Datei über einen JFileChooser.
	 */
	private void exportQuiz(Quiz quiz) {
		// Zur Sicherheit: das Quiz nochmal vollständig laden (inkl. Fragen & Antworten)
		Quiz fullQuiz = quizDAO.findById(quiz.getId());
		if (fullQuiz == null) {
			JOptionPane.showMessageDialog(this, "Quiz konnte nicht geladen werden.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// TXT-Inhalt erstellen
		StringBuilder sb = new StringBuilder();
		sb.append("Quiz: ").append(fullQuiz.getTitel()).append("\n");
		sb.append("Thema: ").append(fullQuiz.getThema() != null ? fullQuiz.getThema().getBezeichnung() : "Kein Thema")
				.append("\n");
		sb.append("Kategorie: ")
				.append(fullQuiz.getKategorie() != null ? fullQuiz.getKategorie().getBezeichnung() : "Keine Kategorie")
				.append("\n\n");

		List<Frage> fragenListe = fullQuiz.getFragenListe();
		if (fragenListe != null && !fragenListe.isEmpty()) {
			for (int i = 0; i < fragenListe.size(); i++) {
				Frage frage = fragenListe.get(i);
				sb.append("Frage ").append(i + 1).append(": ").append(frage.getFragetext()).append("\n");

				if (frage.getAntworten() != null && !frage.getAntworten().isEmpty()) {
					for (Antwort a : frage.getAntworten()) {
						// ( ) vor jeder Antwort, du könntest hier je nach Richtigkeit (x) machen
						sb.append("( ) ").append(a.getAntworttext()).append("\n");
					}
				}
				sb.append("Punkte: ").append(frage.getMaxPunktzahl()).append("\n\n");
			}
		} else {
			sb.append("Keine Fragen vorhanden.\n");
		}

		// Datei speichern über JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		// Default-Dateiname vorschlagen (z. B. quiz_Titel.txt)
		fileChooser.setSelectedFile(new File("quiz_" + fullQuiz.getTitel() + ".txt"));

		int userSelection = fileChooser.showSaveDialog(MyQuizzesPanel.this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
				writer.write(sb.toString());
				writer.flush();
				JOptionPane.showMessageDialog(this, "Export erfolgreich!\nDatei: " + fileToSave.getAbsolutePath(),
						"Erfolg", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Fehler beim Export: " + ex.getMessage(), "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
