package model;

public class Frage extends BaseEntity {

	private String fragetext;
	private Fragetyp fragetyp;
	private int maxPunktzahl;
	private Quiz quiz;

	public Frage(int id, String fragetext, Fragetyp fragetyp, int maxPunktzahl, Quiz quiz) {
		super(id);
		this.fragetext = fragetext;
		this.fragetyp = fragetyp;
		this.maxPunktzahl = maxPunktzahl;
		this.quiz = quiz;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
