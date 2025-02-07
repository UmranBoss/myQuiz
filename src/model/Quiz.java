package model;

import java.util.ArrayList;

public class Quiz extends BaseEntity {

	private String titel;
	private Lehrer lehrer;
	private Kategorie kategorie;
	private Thema thema;
	private ArrayList<Frage> fragenListe;

	// Konstruktor nutzt den Konstruktor der Basisklasse
	public Quiz(int id, String titel, Lehrer lehrer, Kategorie kategorie, Thema thema) {
		super(id); // ID wird in der Basisklasse gespeichert
		this.lehrer = lehrer;
		this.kategorie = kategorie;
		this.thema = thema;
		this.fragenListe = new ArrayList<>();
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

	public ArrayList<Frage> getFragenListe() {
		return fragenListe;
	}

	public void setFragenListe(ArrayList<Frage> fragenListe) {
		this.fragenListe = fragenListe;
	}

}
