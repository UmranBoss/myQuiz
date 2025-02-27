package data.dto;

/**
 * Speichert eine Antwort mit text, score (Punkte) und isCorrect (ob sie richtig
 * ist). Getter & Setter für Zugriff und Modifikation. Konstruktor für einfaches
 * Erstellen. toString() Methode für Debugging.
 */
public class AntwortDTO {

	private String text;
	private int score;
	private boolean isCorrect;

	public AntwortDTO(String text, int score, boolean isCorrect) {
		super();
		this.text = text;
		this.score = score;
		this.isCorrect = isCorrect;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	@Override
	public String toString() {
		return "AntwortDTO{" + "text='" + text + '\'' + ", score=" + score + ", isCorrect=" + isCorrect + '}';
	}
}
