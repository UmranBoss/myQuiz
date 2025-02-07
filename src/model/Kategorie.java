package model;

public class Kategorie extends BaseEntity {

	private String bezeichnung;

	public Kategorie(int id, String bezeichnung) {
		super(id);
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

}
