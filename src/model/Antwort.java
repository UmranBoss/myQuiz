package model;

public class Antwort extends BaseEntity {

	private String antworttext;
	private int punktzahl;
	private boolean richtigkeit;
	private Frage frage;

	public Antwort(String antworttext, int punktzahl, boolean richtigkeit, Frage frage) {
		this.antworttext = antworttext;
		this.punktzahl = punktzahl;
		this.richtigkeit = richtigkeit;
		this.frage = frage;
	}

	public String getAntworttext() {
		return antworttext;
	}

	public void setAntworttext(String antworttext) {
		this.antworttext = antworttext;
	}

	public int getPunktzahl() {
		return punktzahl;
	}

	public void setPunktzahl(int punktzahl) {
		this.punktzahl = punktzahl;
	}

	public boolean isRichtigkeit() {
		return richtigkeit;
	}

	public void setRichtigkeit(boolean richtigkeit) {
		this.richtigkeit = richtigkeit;
	}

	public Frage getFrage() {
		return frage;
	}

	public void setFrage(Frage frage) {
		this.frage = frage;
	}
}
