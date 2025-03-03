package gui;

import java.awt.CardLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import data.dao.AntwortDAO;
import data.dao.FrageDAO;
import data.dao.KategorieDAO;
import data.dao.LehrerDAO;
import data.dao.QuizDAO;
import data.dao.ThemaDAO;
import model.Quiz;

/**
 * {@code BaseFrame} ist das Hauptfenster der Anwendung und verwaltet
 * verschiedene Panels zur Anzeige von Inhalten. Es verwendet ein
 * {@code CardLayout}, um zwischen den verschiedenen Ansichten der Anwendung zu
 * wechseln.
 * 
 * Die Klasse initialisiert die grundlegenden GUI-Komponenten, einschließlich
 * der Quiz-Tabellen, der Frageerstellung und der Anzeige der bereits erstellten
 * Quizzes. Sie enthält auch Methoden, um mit Quizdaten zu arbeiten, darunter
 * das Setzen des aktuellen Quizzes und das Erstellen von Frage-Tabellen.
 * 
 * @see StartPanel
 * @see BaseTab
 * @see CreateQuestionPanel
 * @see MyQuizzesPanel
 * @see QuestionTablePanel
 * @see QuizDAO
 * @see FrageDAO
 */
public class BaseFrame extends JFrame implements Constants {

	/*
	 * Als Instanzvariablen deklarieren, damit sie innerhalb der gesamten Klasse
	 * BaseFrame verfügbar sind und nicht nur innerhalb des Konstruktors.
	 */
	private JPanel mainPanel;
	private CardLayout cardLayout;
	private StartPanel startPanel;
	private BaseTab baseTab;

	private CreateQuestionPanel createQuestionPanel; // hier deklariert
	private Quiz currentQuiz;
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;
	private AntwortDAO antwortDAO;
	private QuestionTablePanel questionTablePanel;
	private MyQuizzesPanel myQuizzesPanel;

	/**
	 * Konstruktor für {@code BaseFrame}. Initialisiert die GUI-Komponenten, setzt
	 * das Icon für das Fenster und richtet das {@code CardLayout} ein.
	 * 
	 * @see CardLayout
	 */
	public BaseFrame() {

		super(FRAME_TITLE);
		antwortDAO = new AntwortDAO();
		frageDAO = new FrageDAO(antwortDAO);
		quizDAO = new QuizDAO(new LehrerDAO(), new KategorieDAO(), new ThemaDAO(), frageDAO, antwortDAO);
		frageDAO.setQuizDAO(quizDAO); // Setze quizDAO in frageDAO, damit diese Referenz nicht null ist
		antwortDAO.setFrageDAO(frageDAO); // WICHTIG: Setze die FrageDAO in der AntwortDAO

		ImageIcon frameIcon = new ImageIcon("src/gui/img/answer.png");
		setIconImage(frameIcon.getImage());
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Initialisierung der Panels und des Layouts
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		currentQuiz = null; // wird später dynamisch gesetzt
		baseTab = new BaseTab(this); // this -> weil dadurch erhält BaseTab Zugriff auf BaseFrame & kann
												// das CardLayout verwenden.
		startPanel = new StartPanel(mainPanel, baseTab);
		createQuestionPanel = new CreateQuestionPanel(currentQuiz, frageDAO, quizDAO, this);
		myQuizzesPanel = new MyQuizzesPanel(this, quizDAO);

		if (currentQuiz == null) {
			System.out.println("FEHLER: Quiz ist NULL, bevor CreateQuestionPanel erstellt wird!");
		} else {
			System.out.println("Quiz existiert mit ID: " + currentQuiz.getId());
		}

		// Panels zum CardLayout hinzufügen
		mainPanel.add(startPanel, "Startseite");
		mainPanel.add(baseTab, "QuizSeiten");
		mainPanel.add(createQuestionPanel, "FrageSeite");
		mainPanel.add(myQuizzesPanel, "MeineQuizzes");

		add(mainPanel);
		setVisible(true);
	}

	/**
	 * Gibt das {@code CardLayout} des Fensters zurück.
	 * 
	 * @return Das {@code CardLayout}.
	 */
	public CardLayout getCardLayout() {
		// TODO Auto-generated method stub
		return cardLayout;
	}

	/**
	 * Gibt das {@code mainPanel} des Fensters zurück.
	 * 
	 * @return Das {@code mainPanel}.
	 */
	public JPanel getMainPanel() {
		// TODO Auto-generated method stub
		return mainPanel;
	}

	/**
	 * Gibt das {@code CreateQuestionPanel} zurück.
	 * 
	 * @return Das {@code CreateQuestionPanel}.
	 */
	public CreateQuestionPanel getFrageSeitePanel() {
		// TODO Auto-generated method stub
		return createQuestionPanel;
	}

	/**
	 * Setzt das aktuell erstellte Quiz und aktualisiert das
	 * {@code CreateQuestionPanel}, damit neue Fragen dem richtigen Quiz hinzugefügt
	 * werden.
	 * 
	 * @param quiz Das aktuell erstellte Quiz.
	 */
	public void setCurrentQuiz(Quiz quiz) {
		this.currentQuiz = quiz;
		createQuestionPanel.setQuiz(quiz);
	}

	/**
	 * Erstellt ein neues {@code QuestionTablePanel} für das angegebene Quiz und
	 * fügt es zum {@code mainPanel} hinzu.
	 * 
	 * @param quiz Das Quiz, für das die Fragen-Tabelle erstellt wird.
	 */
	public void createQuestionTablePanel(Quiz quiz) {
		questionTablePanel = new QuestionTablePanel(this, quiz, frageDAO, quizDAO);
		mainPanel.add(questionTablePanel, "FragenTabelle");
	}

	/**
	 * Gibt das {@code QuestionTablePanel} zurück.
	 * 
	 * @return Das {@code QuestionTablePanel}.
	 */
	public QuestionTablePanel getQuestionTablePanel() {
		// TODO Auto-generated method stub
		return questionTablePanel;
	}

	/**
	 * Gibt das {@code CreateQuestionPanel} zurück.
	 * 
	 * @return Das {@code CreateQuestionPanel}.
	 */
	public CreateQuestionPanel getCreateQuestionPanel() {
		// TODO Auto-generated method stub
		return createQuestionPanel;
	}

	/**
	 * Gibt das {@code MyQuizzesPanel} zurück.
	 * 
	 * @return Das {@code MyQuizzesPanel}.
	 */
	public MyQuizzesPanel getMeineQuizzesPanel() {
		return myQuizzesPanel;
	}

}
