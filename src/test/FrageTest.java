package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Frage;
import model.Fragetyp;
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

class FrageTest {

	private Quiz q;
	private Frage f;

	@BeforeEach
	void setUp() {

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
		Lehrer l = new Lehrer(1, "Max", "Mustermann", "max.mustermann@example.de", "Test123*");
		Kategorie k = new Kategorie(1, "Mathematik");
		Thema t = new Thema(1, "Potenzrechnung");

		q = new Quiz(1, "Grundlagen", l, k, t);
		f = new Frage(1, "Woraus besteht eine Potenz?", Fragetyp.MEHRFACH, 2, q);

	}

	@Test
	void testFrageErstellung() {
		assertEquals(1, f.getId());
		assertEquals("Woraus besteht eine Potenz?", f.getFragetext());
		assertEquals(Fragetyp.MEHRFACH, f.getFragetyp());
		assertEquals(2, f.getMaxPunktzahl());
		assertEquals(1, f.getQuizId());

	}

}
