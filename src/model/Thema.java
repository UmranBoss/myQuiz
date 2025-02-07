package model;

public class Thema extends BaseEntity {

	private String bezeichnung;
	
	public Thema(int id, String bezeichnung) {
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
