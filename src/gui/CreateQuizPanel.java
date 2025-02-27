package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.dao.AntwortDAO;
import data.dao.FrageDAO;
import data.dao.KategorieDAO;
import data.dao.LehrerDAO;
import data.dao.QuizDAO;
import data.dao.ThemaDAO;
import model.Frage;
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

public class CreateQuizPanel extends BasePanel {

	private Quiz q;
	private LehrerDAO lD;
	private KategorieDAO kD;
	private ThemaDAO tD;
	private FrageDAO fD;
	private AntwortDAO aD;
	private QuizDAO qD;

	private BaseFrame frame;

	public CreateQuizPanel(BaseFrame frame) {
		this.frame = frame;

		// DAOs initialisieren
		lD = new LehrerDAO();
		kD = new KategorieDAO();
		tD = new ThemaDAO();
		aD = new AntwortDAO();

		// FrageDAO vorläufig mit null für QuizDAO erstellen
		fD = new FrageDAO(null, aD);

		// QuizDAO erstellen & in FrageDAO injizieren
		QuizDAO qD = new QuizDAO(lD, kD, tD, fD, aD);
		fD.setQuizDAO(qD);
		aD.setFrageDAO(fD);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		// Überschrift
		JLabel headerLabel = new BaseLabel("Erstelle Dein Quiz", 18, true);
		gbc.gridy = 0;
		gbc.gridwidth = 3; // Legt fest, wie viele Spalten eine Komponente einnimmt
		add(headerLabel, gbc);

		// Titel-Feld
		gbc.gridy++;
		gbc.gridwidth = 1;
		add(new BaseLabel("Titel*", 14, true), gbc);

		gbc.gridx = 1;
		BaseTextField titleField = new BaseTextField("Titel eingeben");
		add(titleField, gbc);

//		// Beschreibung
//		gbc.gridy++;
//		gbc.gridx = 0;
//		add(new BaseLabel("Beschreibung*", 14, true), gbc);

//		gbc.gridx = 1;
//		JTextField descField = createPlaceholderTextField("Beschreibung eingeben");
//		add(descField, gbc);

		// Thema-Dropdown
		gbc.gridy++;
		gbc.gridx = 0;
		add(new BaseLabel("Thema*", 14, true), gbc);

		gbc.gridx = 1;
		JComboBox<Thema> themeBox = new JComboBox<Thema>();
		themeBox.addItem(new Thema("Thema auswählen"));
		;
		add(themeBox, gbc);

		// Button zum Hinzufügen eines neuen Themas
		gbc.gridx = 2;
		JButton addThemeButton = new BaseButton("+ Neues Thema hinzufügen");
		add(addThemeButton, gbc);

		// Kategorie-Dropdown
		gbc.gridy++;
		gbc.gridx = 0;
		add(new BaseLabel("Kategorie*", 14, true), gbc);

		gbc.gridx = 1;
		JComboBox<Kategorie> categoryBox = new JComboBox<Kategorie>();
		categoryBox.addItem(new Kategorie("Kategorie auswählen"));
		;
		add(categoryBox, gbc);

		// Button zum Hinzufügen einer neuen Kategorie
		gbc.gridx = 2;
		JButton addCategoryButton = new BaseButton("+ Neue Kategorie hinzufügen");
		add(addCategoryButton, gbc);

		// Buttons unten
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;

		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new BaseButton("Abbrechen");
		JButton saveButton = new BaseButton("Speichern");

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);
		add(buttonPanel, gbc);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Werte aus den TF holen
				String title = titleField.getText();
				// String description = descField.getText();
				int lehrerId = 1; // Platzhalter Dummy - hier kommt die User-Logik erst später

				// Richtiges Objekt aus der ComboBox holen
				Kategorie k = (Kategorie) categoryBox.getSelectedItem();
				Thema t = (Thema) themeBox.getSelectedItem();

				if (title.isEmpty() || title.equals("Titel eingeben")) {
					JOptionPane.showMessageDialog(null, "Bitte Titel eingeben!");
					return;
				}

				// Überprüfung, ob eine gültige Kategorie & ein gültiges Thema ausgewählt wurde
				if (k == null || t == null || k.getId() == 0 || t.getId() == 0) {
					JOptionPane.showMessageDialog(null, "Bitte Thema & Kategorie auswählen!");
					return;
				}

				// IDs aus den Objekten holen
				int kategorieId = k.getId();
				int themaId = t.getId();
				Lehrer l = lD.findById(lehrerId);

				List<Frage> fragenListe = new ArrayList<>();
				q = new Quiz(title, l, k, t, fragenListe);
				QuizDAO qD = new QuizDAO(lD, kD, tD, fD, aD);
				boolean success = qD.create(q); // das bereits gespeicherte Quiz verwenden
				System.out.println("📌 DEBUG: Fragen für Quiz " + q.getId() + ": " + fD.findQuizById(q.getId()));

				if (success) {

					JOptionPane.showMessageDialog(null, "Quiz gespeichert!");
					frame.getFrageSeitePanel().setQuiz(q);

					frame.getCardLayout().show(frame.getMainPanel(), "FrageSeite");

					System.out.println("FrageSeite wird angezeigt!");
				} else {
					JOptionPane.showMessageDialog(null, "Quiz konnte nicht gespeichert werden.");
				}

			}
		});

		// Listener für "Neues Thema hinzufügen"
		addThemeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newTheme = JOptionPane.showInputDialog("Neues Thema eingeben:");
				if (newTheme != null && !newTheme.trim().isEmpty()) {
					// Neues Thema in die DB einfügen
					ThemaDAO themaDAO = new ThemaDAO();
					Thema thema = new Thema(newTheme); // Neues Thema erstellen
					themaDAO.create(thema); // Thema speichern
					themeBox.addItem(thema); // Das neue Thema im Dropdown
					JOptionPane.showMessageDialog(null,
							"Dein Eintrag '" + thema + "' wurde der Dropdown-Liste hinzugefügt!");
				} else {
					JOptionPane.showMessageDialog(null, "Bitte Thema eintragen!");
				}
			}
		});

		// Listener für "Neue Kategorie hinzufügen"
		addCategoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newCategory = JOptionPane.showInputDialog("Neue Kategorie eingeben:");
				if (newCategory != null && !newCategory.trim().isEmpty()) {
					// Hier fügst du die neue Kategorie in die Datenbank ein
					KategorieDAO kategorieDAO = new KategorieDAO();
					Kategorie kategorie = new Kategorie(newCategory); // Neue Kategorie erstellen
					kategorieDAO.create(kategorie); // Kategorie speichern
					categoryBox.addItem(kategorie); // Die neue Kategorie im Dropdown hinzufügen
					JOptionPane.showMessageDialog(null,
							"Dein Eintrag '" + kategorie + "' wurde der Dropdown-Liste hinzugefügt");
				} else {
					JOptionPane.showMessageDialog(null, "Bitte Kategorie eintragen!");
				}
			}
		});
	}

	private JTextField createPlaceholderTextField(String placeholder) {
		JTextField textField = new JTextField(placeholder);
		textField.setForeground(Color.LIGHT_GRAY);
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText(placeholder);
					textField.setForeground(Color.LIGHT_GRAY);
				}
			}
		});

		return textField;
	}

}