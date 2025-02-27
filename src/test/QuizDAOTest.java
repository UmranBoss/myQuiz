package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.DBConnection;
import data.dao.AntwortDAO;
import data.dao.FrageDAO;
import data.dao.KategorieDAO;
import data.dao.LehrerDAO;
import data.dao.QuizDAO;
import data.dao.ThemaDAO;
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

class QuizDAOTest {

	private Connection conn;
	private LehrerDAO lehrerDAO;
	private QuizDAO quizDAO;
	private KategorieDAO kategorieDAO;
	private ThemaDAO themaDAO;
	private FrageDAO frageDAO;
	private AntwortDAO antwortDAO;

	private Lehrer testL;
	private Kategorie testK;
	private Thema testT;
	private Quiz testQ;

	@BeforeEach
	void setUp() throws Exception {
		conn = DBConnection.getInstance().getConnection();
		conn.setAutoCommit(false); // verhindert dauerhafte Änderung in der DB

		lehrerDAO = new LehrerDAO();
		kategorieDAO = new KategorieDAO();
		themaDAO = new ThemaDAO();
		// QuizDAO mit FrageDAO korrekt instanziieren
		frageDAO = new FrageDAO(null, antwortDAO); // FrageDAO wird benötigt, stelle sicher, dass es korrekt funktioniert
		quizDAO = new QuizDAO(lehrerDAO, kategorieDAO, themaDAO, frageDAO,antwortDAO); // quizDAO korrekt instanziieren mit
																			// frageDAO
		clearDatabbase();

		// Testdaten initialisieren
		initTestData();
	}

	@AfterEach
	void tearDown() throws Exception {
		conn.rollback(); // Änderungen zurücksetzen
		conn.setAutoCommit(true);
	}

	private void clearDatabbase() {
		try (Statement stmt = conn.createStatement()) {
			stmt.execute("DELETE FROM frage;");
			stmt.execute("DELETE FROM quiz;");
			stmt.execute("DELETE FROM thema;");
			stmt.execute("DELETE FROM kategorie;");
			stmt.execute("DELETE FROM lehrer;");

			// Zurücksetzen der Auto-Increment-Werte (je nach Datenbank-Typ notwendig)
			stmt.execute("ALTER TABLE frage AUTO_INCREMENT = 1;");
			stmt.execute("ALTER TABLE quiz AUTO_INCREMENT = 1;");
			stmt.execute("ALTER TABLE thema AUTO_INCREMENT = 1;");
			stmt.execute("ALTER TABLE kategorie AUTO_INCREMENT = 1;");
			stmt.execute("ALTER TABLE lehrer AUTO_INCREMENT = 1;");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Fehler beim Bereinigen der Datenbank: " + e.getMessage());
		}
	}

	// Testdaten einmalig erstellen & in der DB speichern
	private void initTestData() {
		testL = new Lehrer("Sarah", "Rili", "sarah_rili@example", "123"); // Objekt erstellen
		lehrerDAO.create(testL); // In die DB einfügen
		testK = new Kategorie("Mathematik");
		kategorieDAO.create(testK);
		testT = new Thema("Potenzrechnung");
		themaDAO.create(testT);
		testQ = new Quiz("Definition", testL, testK, testT, new ArrayList<>()); // Leere Liste übergeben
		quizDAO.create(testQ);
	}

	@Test
	void testCreateQuiz() {
		Quiz newQuiz = new Quiz("Algebra", testL, testK, testT, new ArrayList<>());
		boolean created = quizDAO.create(newQuiz);

		// Check, ob das Quiz erstellt wurde
		assertTrue("Quiz sollte erstellt worden sein!", created);
		assertNotNull("Quiz-ID sollte gesetzt werden.", newQuiz.getId());

		// Überprüfen, ob das Quiz in der DB gespeichert wurde
		Quiz retrievedQuiz = quizDAO.findById(newQuiz.getId());
		assertNotNull("Quiz sollte in der DB existieren.", retrievedQuiz);
		assertEquals("Titel des Quiz sollte Algebra sein.", "Algebra", retrievedQuiz.getTitel());
	}

	@Test
	void testFindById() {
		Quiz findQuiz = quizDAO.findById(testQ.getId());

		// Check, ob das Quiz abgerufen wurde
		assertNotNull("Quiz mit der ID " + testQ.getId() + " sollte existieren.", findQuiz);
		assertEquals("Titel des Quiz sollte 'Definition' sein.", "Definition", findQuiz.getTitel());

		// Teste auch das Verhalten für eine ungültige ID
		Quiz nonExistentQuiz = quizDAO.findById(-1); // Eine nicht existierende ID
		assertNull("Kein Quiz sollte für die ID -1 existieren.", nonExistentQuiz);
	}

	@Test
	void testDeleteQuiz() {
		boolean deleted = quizDAO.delete(testQ.getId());
		assertTrue("Quiz sollte gelöscht werden.", deleted);

		// Überprüfen, dass das Quiz nach dem Löschen nicht mehr existiert
		assertNull("Das Quiz sollte nun nicht mehr existieren.", quizDAO.findById(testQ.getId()));

		// Testen, ob das Löschen auch bei einer ungültigen ID funktioniert
		boolean deletedNonExistent = quizDAO.delete(-1); // Ungültige ID
		assertFalse("Das Löschen einer nicht existierenden ID sollte fehlschlagen.", deletedNonExistent);
	}
}
