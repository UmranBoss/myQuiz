package model;

import java.util.ArrayList;
import java.util.List;

public class Frage extends BaseEntity {

	private String fragetext;
	private Fragetyp fragetyp;
	private int maxPunktzahl;
	private Quiz quiz;
	private List<Antwort> antworten;

	public Frage(String fragetext, Fragetyp fragetyp, int maxPunktzahl, Quiz quiz) {
		this.fragetext = fragetext;
		this.fragetyp = fragetyp;
		this.maxPunktzahl = maxPunktzahl;
		this.quiz = quiz;
	}

	public String getFragetext() {
		return fragetext;
	}

	public void setFragetext(String fragetext) {
		this.fragetext = fragetext;
	}

	public Fragetyp getFragetyp() {
		return fragetyp;
	}

	public void setFragetyp(Fragetyp fragetyp) {
		this.fragetyp = fragetyp;
	}

	public int getMaxPunktzahl() {
		return maxPunktzahl;
	}

	public void setMaxPunktzahl(int maxPunktzahl) {
		this.maxPunktzahl = maxPunktzahl;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Integer getQuizId() {
		return quiz.getId();
	}

	public List<Antwort> getAntworten() {
		return antworten;
	}

	public void setAntworten(List<Antwort> antworten) {
		this.antworten = antworten;
	}

	public void addAntwort(Antwort antwort) {
		if (this.antworten == null) {
			this.antworten = new ArrayList<>();
		}
		this.antworten.add(antwort);
	}

}
