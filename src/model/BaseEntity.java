package model;

public abstract class BaseEntity {

	protected int id; // ID-Attribut für alle Entitäten

	public BaseEntity(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
