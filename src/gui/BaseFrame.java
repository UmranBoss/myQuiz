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
import model.Frage;
import model.Quiz;

public class BaseFrame extends JFrame implements Constants {

	/*
	 * Als Instanzvariablen deklarieren, damit sie innerhalb der gesamten Klasse
	 * BaseFrame verfügbar sind und nicht nur innerhalb des Konstruktors.
	 */
	private JPanel mainPanel;
	private CardLayout cardLayout;
	private StartPanel startPanel;
	private QuizTabPanel quizTabsPanel;

	private CreateQuestionPanel createQuestionPanel; // hier deklariert
	private Quiz currentQuiz;
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;
	private AntwortDAO antwortDAO;
	private QuestionTablePanel questionTablePanel;
	private MyQuizzesPanel myQuizzesPanel;

	public BaseFrame() {

		super(FRAME_TITLE);
		antwortDAO = new AntwortDAO();
		frageDAO = new FrageDAO(antwortDAO);
		quizDAO = new QuizDAO(new LehrerDAO(), new KategorieDAO(), new ThemaDAO(), frageDAO, antwortDAO);
		
		
		// Setze quizDAO in frageDAO, damit diese Referenz nicht null ist
		frageDAO.setQuizDAO(quizDAO);

		// WICHTIG: Setze die FrageDAO in der AntwortDAO
		antwortDAO.setFrageDAO(frageDAO);

		ImageIcon frameIcon = new ImageIcon("src/gui/img/answer.png");
		setIconImage(frameIcon.getImage());

		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// CardLayout und mainPanel initialisieren
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout); // Nur einmal initialisieren

		// Kein festes Quiz initialisieren – es wird später dynamisch gesetzt, wenn ein
		// neues Quiz erstellt wurde.
		currentQuiz = null;

		// Panels initialisieren
		quizTabsPanel = new QuizTabPanel(this); // this -> weil dadurch erhält QuizTabPanel Zugriff auf BaseFrame und
												// kann das CardLayout verwenden.
		startPanel = new StartPanel(mainPanel, quizTabsPanel); // StartPanel initialisieren

		createQuestionPanel = new CreateQuestionPanel(currentQuiz, frageDAO, quizDAO, this); // hier initialisiert
		myQuizzesPanel = new MyQuizzesPanel(this, quizDAO);

		if (currentQuiz == null) {
			System.out.println("FEHLER: Quiz ist NULL, bevor CreateQuestionPanel erstellt wird!");
		} else {
			System.out.println("Quiz existiert mit ID: " + currentQuiz.getId());
		}

		// Panels zum CardLayout hinzufügen
		mainPanel.add(startPanel, "Startseite");
		mainPanel.add(quizTabsPanel, "QuizSeiten");
		mainPanel.add(createQuestionPanel, "FrageSeite"); // hier geaddet
//		mainPanel.add(questionTablePanel, "FragenTabelle");
		mainPanel.add(myQuizzesPanel, "MeineQuizzes");

		add(mainPanel);
		setVisible(true);
	}

	public CardLayout getCardLayout() {
		// TODO Auto-generated method stub
		return cardLayout;
	}

	public JPanel getMainPanel() {
		// TODO Auto-generated method stub
		return mainPanel;
	}

	public CreateQuestionPanel getFrageSeitePanel() {
		// TODO Auto-generated method stub
		return createQuestionPanel;
	}

	/**
	 * Mit dieser Methode wird das aktuell erstellte Quiz gesetzt. Gleichzeitig wird
	 * das CreateQuestionPanel aktualisiert, sodass Fragen zum richtigen Quiz
	 * hinzugefügt werden.
	 */
	public void setCurrentQuiz(Quiz quiz) {
		this.currentQuiz = quiz;
		createQuestionPanel.setQuiz(quiz);
	}

	public void createQuestionTablePanel(Quiz quiz) {
		questionTablePanel = new QuestionTablePanel(this, quiz, frageDAO, quizDAO);
		mainPanel.add(questionTablePanel, "FragenTabelle");
	}

	// Getter, damit CreateQuestionPanel darauf zugreifen kann
	public QuestionTablePanel getQuestionTablePanel() {
		// TODO Auto-generated method stub
		return questionTablePanel;
	}

	// Getter, damit CreateQuestionPanel darauf zugreifen kann
	public CreateQuestionPanel getCreateQuestionPanel() {
		// TODO Auto-generated method stub
		return createQuestionPanel;
	}

	// Getter für das MeineQuizzesPanel
	public MyQuizzesPanel getMeineQuizzesPanel() {
		return myQuizzesPanel;
	}

}
