package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.DBConnection;

public abstract class BaseDAO<T> {
	protected Connection connection;

	public BaseDAO() {
		// Verbindung zur DB herstellen
		this.connection = DBConnection.getInstance().getConnection();
	}

	// CREATE
	public abstract boolean create(T entity);

	// READ
	public abstract T findById(int id);

	// UPDATE
	public abstract boolean update(T entity);

	// DELETE
	public abstract boolean delete(int id);

	// Allgemeine Methode zum Ausführen von SQL-Abfragen und Mapping von Ergebnissen
	protected List<T> executeQuery(String query, Object[] params, ResultSetMapper<T> mapper) {
		List<T> result = new ArrayList<>();
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			// Setze die Parameter im PreparedStatement (wenn vorhanden)
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					result.add(mapper.map(rs)); // Mapping der ResultSet-Daten auf das Entitätsobjekt
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return result;
	}

	// Generische Schnittstelle für das Mapping von ResultSets auf Entities
	public interface ResultSetMapper<T> {

		T map(ResultSet rs) throws SQLException;
	}

}
