package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/*
 * Die Konstante nutze ich derzeit nicht, kommt später zum Einsatz
 */
public interface Constants {

	public static final String FRAME_TITLE = "bbQQuiz";

	// SLOGAN
	public static final Font FONT_SLOGAN = new Font("Comic Sans Ms", Font.BOLD, 26);
	public static final Color COLOR_SLOGAN = new Color(235, 64, 52);
	public static final String LABEL_SLOGAN = "Hallo & ♥-lich Willkommen bei bbQQuiz";

	// HEADER
	public static final Font FONT_HEADER = new Font("Comic Sans Ms", Font.BOLD, 18);
	public static final Color COLOR_HEADER = new Color(235, 64, 52);
	public static final String LABEL_PANEL1_HEADER1 = "Erstelle Dein Quiz";
	// TITLE
	public static final Font FONT_TITLE = new Font("Comic Sans Ms", Font.PLAIN, 14);
	public static final Color COLOR_TITLE = new Color(33, 33, 32);
	public static final String LABEL_PANEL1_TITEL = "Titel*";
	public static final String LABEL_PANEL1_BESCHREIBUNG = "Beschreibung**";
	public static final String LABEL_PANEL1_THEMA = "Thema*";
	public static final String LABEL_PANEL1_KATEGORIE = "Kategorie*";

	// TEXT
	public static final Font FONT_TEXT = new Font("Comic Sans MS", Font.PLAIN, 16);
	public static final Color COLOR_TEXT = new Color(33, 33, 32);
	public static final String TEXT_STARTSEITE_TXT1 = "Die einfache Möglichkeit, Quizze zu erstellen & das Lernen Deiner Schüler interaktiv zu gestalten!";

	// TAB
	public static final Font FONT_TAB = new Font("Comic Sans Ms", Font.BOLD, 12);
	public static final Color COLOR_TAB = new Color(235, 64, 52);
	public static final String LABEL_TAB1 = "Quizze erstellen";
	public static final String LABEL_TAB2 = "Meine Quizze";
	
	// TEXTFELDER
	public static final Dimension TEXTFIELD_SIZE = new Dimension(250, 25);
	public static final Font TEXTFIELD_FONT = new Font("Comic Sans MS", Font.PLAIN, 12);
	public static final Color TEXTFIELD_COLOR = Color.DARK_GRAY;

	// SPINNER
	public static final Dimension SPINNER_SIZE = new Dimension(25, 25);
	public static final Font SPINNER_FONT = new Font("Comic Sans MS", Font.PLAIN, 12);
	public static final Color SPINNER_COLD = Color.DARK_GRAY;

}
