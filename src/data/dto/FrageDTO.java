package data.dto;

import java.util.List;

/**
 * Speichert eine Frage mit text, isSingleChoice (Einzel- oder Mehrfachwahl) und
 * einer Liste von AntwortDTO. Getter & Setter für Zugriff und Modifikation.
 * Konstruktor für einfaches Erstellen. toString() Methode für Debugging.
 */
public class FrageDTO {

	private String text;
	private boolean isSingleChoice;
	private List<AntwortDTO> antworten;

	public FrageDTO(String text, boolean isSingleChoice, List<AntwortDTO> antworten) {
		super();
		this.text = text;
		this.isSingleChoice = isSingleChoice;
		this.antworten = antworten;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSingleChoice() {
		return isSingleChoice;
	}

	public void setSingleChoice(boolean isSingleChoice) {
		this.isSingleChoice = isSingleChoice;
	}

	public List<AntwortDTO> getAntworten() {
		return antworten;
	}

	public void setAntworten(List<AntwortDTO> antworten) {
		this.antworten = antworten;
	}

	@Override
	public String toString() {
		return "FrageDTO{" + "text='" + text + '\'' + ", isSingleChoice=" + isSingleChoice + ", antworten=" + antworten
				+ '}';
	}
}
