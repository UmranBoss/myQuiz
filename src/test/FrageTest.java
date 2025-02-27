package test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.DBConnection;
import data.dao.AntwortDAO;
import data.dao.FrageDAO;
import data.dao.KategorieDAO;
import data.dao.LehrerDAO;
import data.dao.QuizDAO;
import data.dao.ThemaDAO;
import model.Frage;
import model.Fragetyp;
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

class FrageTest {

	private List<Frage> fragen;
	private Quiz q;
	private FrageDAO frageDAO;
	private QuizDAO quizDAO;
	private AntwortDAO aD;

	private Connection conn;

	@BeforeEach
	void setUp() throws Exception {
		conn = DBConnection.getInstance().getConnection();
		conn.setAutoCommit(false); // verhindert dauerhafte Änderung in der DB

		cleardatabase();

		/*
		 * Erstellen von Objekten: Du erstellst zuerst mehrere Objekte (Lehrer,
		 * Kategorie, Thema), die du später als Argumente für die Erstellung des
		 * Quiz-Objekts benötigst.
		 * 
		 * Weitergabe von Objekten: Beim Erstellen des Quiz-Objekts übergibst du die
		 * Objekte (lehrer, kategorie, thema) an den Konstruktor des Quiz. Ebenso
		 * übergibst du das erstellte Quiz-Objekt an den Konstruktor der Frage-Klasse.
		 * 
		 * Verkettung von Objekten: In deinem Beispiel erzeugst du also ein Netz von
		 * Objekten, die miteinander verbunden sind: Ein Quiz besteht aus einem Lehrer,
		 * einer Kategorie und einem Thema, und eine Frage gehört zu einem Quiz.
		 */

		// Testdaten vorbereiten
		Lehrer l = new Lehrer("Max", "Mustermann", "maxii.mustermann@example.de", "Test123*");
		Kategorie k = new Kategorie("Mathematik");
		Thema t = new Thema("Potenzrechnung");

		LehrerDAO lD = new LehrerDAO();
		lD.create(l);
		KategorieDAO kD = new KategorieDAO();
		kD.create(k);
		ThemaDAO tD = new ThemaDAO();
		tD.create(t);

		frageDAO = new FrageDAO(quizDAO,aD);
		quizDAO = new QuizDAO(lD, kD, tD, new FrageDAO(quizDAO,aD),aD);

		fragen = new ArrayList<>();

		// 10 Quiz und Fragen erstellen
		for (int i = 1; i <= 10; i++) {
			Quiz quiz = new Quiz("Fragebogen " + i, l, k, t, new ArrayList<>());
			quizDAO.create(quiz);

			if (i == 1) { // das erste Quiz speichern
				this.q = quiz;
			}
			System.out.println("🔍 Quiz nach create(): " + (quiz != null ? "ID=" + quiz.getId() : "NULL"));

			Frage frage = new Frage("Fraglein " + i + "?", Fragetyp.MEHRFACHWAHL, 2, quiz);
			frageDAO.create(frage);
		}
	}

	private void cleardatabase() {
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

	@Test
	void testFrageErstellung() {
		System.out.println("🚀 Test beginnt...");

		System.out.println("🔍 Quiz-ID vor Abfrage: " + q.getId());
		// Hier direkt die Methode aufrufen, um zu testen
		List<Frage> fragen = frageDAO.findQuizById(q.getId());
		System.out.println("📌 Anzahl gefundener Fragen: " + fragen.size());

		assertEquals(10, fragen.size()); // Sicherstellen, dass 10 Fragen erstellt wurden.
	}
//        assertNotNull(fragen);       // Überprüfen, ob die Liste existiert
//        assertEquals(10, fragen.size()); // Sicherstellen, dass 10 Fragen erstellt wurden
//        
//        Frage f = fragen.get(0); // Erste Frage zum Testen nehmen
//        
//        assertNotNull(f); // Sicherstellen, dass die Frage nicht null ist
//        assertEquals(1, f.getId()); // Falls IDs auto-increment sind, sollte die erste Frage ID = 1 haben
//        assertEquals("Question 1?", f.getFragetext());
//        assertEquals(Fragetyp.MEHRFACHWAHL, f.getFragetyp());
//        assertEquals(2, f.getMaxPunktzahl());
//        assertEquals(1, f.getQuiz().getId()); // Sicherstellen, dass sie zum richtigen Quiz gehört
//    }
//	@Test
//	void testFrageErstellung() {
//		assertEquals(1, f.getId()); // Überprüfen, dass die ID nicht 0 ist (d.h., sie wurde gesetzt)
//		assertEquals("Woraus besteht eine Potenz?", f.getFragetext());
//		assertEquals(Fragetyp.MEHRFACHWAHL, f.getFragetyp());
//		assertEquals(2, f.getMaxPunktzahl());
//		assertEquals(q.getId(), f.getQuiz().getId()); // Überprüfen, dass die Quiz-ID korrekt zugewiesen wurde
//
//	}

}
