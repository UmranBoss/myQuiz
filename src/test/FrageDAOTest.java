package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import model.Frage;
import model.Fragetyp;
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

class FrageDAOTest {

	private Connection conn;
	private FrageDAO fD;
	private QuizDAO qD;
	private KategorieDAO kD;
	private ThemaDAO tD;
	private LehrerDAO lD;
	private AntwortDAO aD;

	private Quiz q;
	private Frage f;

	@BeforeEach
	void setUp() throws Exception {
		conn = DBConnection.getInstance().getConnection();
		conn.setAutoCommit(false);
		

		lD = new LehrerDAO();
		kD = new KategorieDAO();
		tD = new ThemaDAO();
		qD = new QuizDAO(lD, kD, tD, fD, aD);
		fD = new FrageDAO(qD,aD);

		Lehrer l = new Lehrer("Alaeim", "Hoauezu", "sjcaekjacg@mail.de", "15aekskcjsg24");
		lD.create(l);
		Kategorie k = new Kategorie("Programmiersprachen");
		kD.create(k);
		Thema t = new Thema("Java");
		tD.create(t);

		// Eine leere Liste von Fragen erstellen
		List<Frage> fragenListe = new ArrayList<>();

		// Quiz erstellen
		q = new Quiz("Test", l, k, t, fragenListe);
		// QuizDAO initialisieren, und FrageDAO mit QuizDAO verbinden
		qD = new QuizDAO(lD, kD, tD, fD,aD);
		fD = new FrageDAO(qD,aD);

		// Quiz in die DB Speichern
		qD.create(q);
	}

	@AfterEach
	void tearDown() throws Exception {
		conn.rollback();
		conn.setAutoCommit(true);
	}

	@Test
	void testErstelleFrage() {
		// eine Frage erstellen
		f = new Frage("Welche Schleife wird in Java verwendet, um eine Anweisung mehrmals auszuführen?",
				Fragetyp.EINZELWAHL, 10, q);
		// Frage zur Liste des Quiz adden
		q.getFragenListe().add(f);
		// Frage in der DB speichern
		fD.create(f);
		// Überprüfen, ob die Frage korrekt gespeichert wurde
		assertNotNull(f.getId()); // Sicherstellen, dass die Frage eine ID erhalten hat
		// Quiz mit der Frage erneut speichern (wenn auch die Fragenliste gespeichert
		// werden soll)
		qD.update(q);
	}

}
