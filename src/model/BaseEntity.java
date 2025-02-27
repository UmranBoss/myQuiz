package model;

public abstract class BaseEntity {

	protected int id; // ID-Attribut für alle Entitäten

	/*
	 * Konstruktor ohne Parameter: Die ID wird nach der Speicherung des Objekts in
	 * der DB von der DB selbst vergeben.
	 */
	public BaseEntity() {
		// Die ID wird von der DB gesetzt
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
