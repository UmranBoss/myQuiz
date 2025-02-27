package model;

public class Kategorie extends BaseEntity {

	private String bezeichnung;

	public Kategorie(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	@Override
	public String toString() {
		return bezeichnung;
	}
}
