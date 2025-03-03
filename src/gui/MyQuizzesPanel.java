package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.dao.QuizDAO;
import gui.QuestionTablePanel.IconRenderer;
import model.Antwort;
import model.Frage;
import model.Fragetyp;
import model.Quiz;

/**
 * Das Panel zur Anzeige und Verwaltung der erstellten Quizzes.
 * 
 * Dieses Panel zeigt eine Tabelle mit einer Übersicht aller erstellten Quizzes,
 * die aus der Datenbank geladen werden. Es ermöglicht das Bearbeiten (noch
 * nicht implementiert), Löschen und Exportieren von Quizzes sowie das Anzeigen
 * einer Vorschau der Quizzes.
 */

public class MyQuizzesPanel extends JPanel {

	private JTable table;
	private DefaultTableModel tableModel;
	private QuizDAO quizDAO;
	private BaseFrame frame;
	private List<Quiz> quizzes;

	/**
	 * Konstruktor für das MyQuizzesPanel.
	 * 
	 * @param frame   Das übergeordnete Frame, in dem das Panel angezeigt wird.
	 * @param quizDAO Das DAO-Objekt zur Interaktion mit der Quiz-Datenbank.
	 */
	public MyQuizzesPanel(BaseFrame frame, QuizDAO quizDAO) {
		this.frame = frame;
		this.quizDAO = quizDAO;
		initComponents();
	}

	/**
	 * Initialisiert die Komponenten des Panels, wie z. B. die Tabelle, Buttons und
	 * die Layout-Konfiguration.
	 */
	private void initComponents() {
		setLayout(new BorderLayout());

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

					switch (col) {
					case 3: // Vorschau
						JOptionPane.showMessageDialog(MyQuizzesPanel.this,
								"Vorschau des Quiz: " + selectedQuiz.getTitel() + "\n\n"
										+ "Vorschau-Funktion ist derzeit nicht verfügbar.");
						break;

					case 4: // Bearbeiten (Implementation für später)
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
	}

	/**
	 * Lädt alle Quizzes aus der Datenbank und befüllt die Tabelle.
	 */
	public void loadTableData() {
		tableModel.setRowCount(0); // Tabelle leeren
		quizzes = quizDAO.findAll(); // Quizze laden

		// Icons laden & skalieren;
		ImageIcon previewIcon = scaleImageIcon("src/gui/img/eye.png", 20, 20);
		ImageIcon editIcon = scaleImageIcon("src/gui/img/edit.png", 20, 20);
		ImageIcon binIcon = scaleImageIcon("src/gui/img/recycling-bin.png", 20, 20);
		ImageIcon exportIcon = scaleImageIcon("src/gui/img/export.png", 20, 20);

		if (quizzes != null) {
			for (Quiz quiz : quizzes) {
				Object[] rowData = { quiz.getTitel(), (quiz.getThema() != null) ? quiz.getThema().getBezeichnung() : "",
						(quiz.getKategorie() != null) ? quiz.getKategorie().getBezeichnung() : "", previewIcon, // Vorschau
						editIcon, // Bearbeiten
						binIcon, // Löschen
						exportIcon // Exportieren
				};
				tableModel.addRow(rowData);
			}
		}
		// Renderer für Icon-Zellen setzen
		table.getColumnModel().getColumn(3).setCellRenderer(new IconRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new IconRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new IconRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new IconRenderer());
	}

	/**
	 * Skaliert ein ImageIcon auf die gewünschte Breite und Höhe.
	 *
	 * @param path   Der Dateipfad des Bildes.
	 * @param width  Die gewünschte Breite des Icons.
	 * @param height Die gewünschte Höhe des Icons.
	 * @return Das skalierte ImageIcon.
	 */
	private ImageIcon scaleImageIcon(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImg);
	}

	/**
	 * Ein benutzerdefinierter TableCellRenderer für die Anzeige von Icons in einer
	 * JTable. Die Icons werden dabei mittig innerhalb der Zelle ausgerichtet.
	 */
	class IconRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value instanceof ImageIcon) {
				JLabel label = new JLabel((ImageIcon) value);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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

				boolean isMultipleChoice = frage.getFragetyp() == Fragetyp.MEHRFACHWAHL;

				if (frage.getAntworten() != null && !frage.getAntworten().isEmpty()) {
					for (Antwort a : frage.getAntworten()) {
						if (isMultipleChoice) {
							sb.append("[ ] ").append(a.getAntworttext()).append("\n");
						} else {
							sb.append("( )").append(a.getAntworttext()).append("\n");
						}
					}
				}
				sb.append("Punkte: ").append(frage.getMaxPunktzahl()).append("\n\n");
			}
		} else {
			sb.append("Keine Fragen vorhanden.\n");
		}

		// Datei speichern über JFileChooser
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setSelectedFile(new File("bbQQuiz_" + fullQuiz.getTitel() + ".txt"));

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
