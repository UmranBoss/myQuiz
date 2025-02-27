package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz extends BaseEntity {

	private String titel;
	private Lehrer lehrer;
	private Kategorie kategorie;
	private Thema thema;
	private List<Frage> fragenListe;

	// Konstruktor nutzt den Konstruktor der Basisklasse
	public Quiz(String titel, Lehrer lehrer, Kategorie kategorie, Thema thema, List<Frage> fragenListe) {
		this.titel = titel;
		this.lehrer = lehrer;
		this.kategorie = kategorie;
		this.thema = thema;
		this.fragenListe = new ArrayList<>(fragenListe); // Umwandlung von List in ArrayList
	}

	public Quiz(int id) {
		this.id = id; // Direkt die ID aus der BaseEntity setzen
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public Lehrer getLehrer() {
		return lehrer;
	}

	public void setLehrer(Lehrer lehrer) {
		this.lehrer = lehrer;
	}

	public Kategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}

	public Thema getThema() {
		return thema;
	}

	public void setThema(Thema thema) {
		this.thema = thema;
	}

	public List<Frage> getFragenListe() {
		return fragenListe;
	}

	public void setFragenListe(List<Frage> fragenListe) {
		this.fragenListe = fragenListe;
	}

}
