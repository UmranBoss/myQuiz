package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.dao.FrageDAO;
import data.dao.QuizDAO;
import model.Frage;
import model.Quiz;

/**
 * Panel, das eine Tabelle von Fragen für ein Quiz anzeigt und ermöglicht, diese
 * zu bearbeiten oder zu löschen.
 */
public class QuestionTablePanel extends JPanel {

	private Quiz currentQuiz;
	private List<Frage> fragenListe;
	private JTable table;
	private DefaultTableModel tableModel;

	private BaseFrame frame;
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;
	/**
	 * Konstruktor für das Frage-Tabellen-Panel.
	 * 
	 * @param frame    Das Hauptfenster, das das Panel enthält
	 * @param quiz     Das Quiz, zu dem die Fragen gehören
	 * @param frageDAO DAO-Objekt für die Frage-Datenbankoperationen
	 * @param quizDAO  DAO-Objekt für die Quiz-Datenbankoperationen
	 */
	private CreateQuestionPanel createQuestionPanel;

	public QuestionTablePanel(BaseFrame frame, Quiz quiz, FrageDAO frageDAO, QuizDAO quizDAO) {
		this.frame = frame;
		this.currentQuiz = quiz;
		this.frageDAO = frageDAO;
		this.quizDAO = quizDAO;

		if (currentQuiz != null) {
			this.fragenListe = currentQuiz.getFragenListe();
		}

		initComponents();
	}

	/**
	 * Initialisiert die Komponenten des Panels.
	 */
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

		// Renderer für Icons setzen
		table.setDefaultRenderer(ImageIcon.class, new IconRenderer());

		// Daten in die Tabelle laden
		loadTableData();

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

			frame.getMeineQuizzesPanel().loadTableData(); // loadTableData()-Methode aufrufen, um MyQuizzesPanel zu
															// aktualiseren
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

	/**
	 * Lädt die Daten in die Tabelle und fügt alle Fragen des aktuellen Quiz hinzu.
	 * Dabei werden Icons für Vorschau, Bearbeiten und Löschen genutzt.
	 */
	void loadTableData() {
		// Tabelle leeren
		tableModel.setRowCount(0);

		// Icons laden & skalieren;
		ImageIcon previewIcon = scaleImageIcon("src/gui/img/eye.png", 20, 20);
		ImageIcon editIcon = scaleImageIcon("src/gui/img/edit.png", 20, 20);
		ImageIcon binIcon = scaleImageIcon("src/gui/img/recycling-bin.png", 20, 20);

		System.out.println("Bild geladen: " + (previewIcon.getIconWidth() > 0));

		if (fragenListe != null) {
			for (Frage frage : fragenListe) {
				Object[] rowData = { frage.getFragetext(), previewIcon, // Vorschau-Symbol
						editIcon, // Bearbeiten (deaktiviert)
						binIcon // Löschen
				};
				tableModel.addRow(rowData);
			}
		}
		// Renderer für Icon-Zellen setzen
		table.getColumnModel().getColumn(1).setCellRenderer(new IconRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new IconRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new IconRenderer());
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
	 * Öffnet ein Dialogfenster, um die Frage (und Antworten) anzuzeigen.
	 * 
	 * @param frage Die Frage, deren Vorschau angezeigt werden soll.
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
	 * Löscht die Frage aus der Datenbank und aktualisiert die Tabelle.
	 * 
	 * @param frage Die Frage, die gelöscht werden soll.
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
