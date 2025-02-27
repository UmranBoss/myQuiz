package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import data.DBConnection;
import model.Kategorie;

public class KategorieDAO extends BaseDAO<Kategorie> {

	private final Connection conn;

	public KategorieDAO() {
		this.conn = DBConnection.getInstance().getConnection();
	}

	@Override
	public boolean create(Kategorie k) {
		String query = "INSERT INTO kategorie (bezeichnung) VALUES (?)";
		try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, k.getBezeichnung());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						k.setId(generatedKeys.getInt(1));
					}
				}
			}
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Kategorie findById(int id) {
		String query = "SELECT * FROM kategorie WHERE id = ?";
		List<Kategorie> kategorieList = executeQuery(query, new Object[] { id }, rs -> {
			// Kategorie-Objekt ohne ID erstellen
			Kategorie k = new Kategorie(rs.getString("bezeichnung"));
			k.setId(rs.getInt("id")); // Die ID wird von der DB gesetzt
			return k;
		});
		return kategorieList.isEmpty() ? null : kategorieList.get(0);
	}

	@Override
	public boolean update(Kategorie k) {
		String query = "UPDATE kategorie SET bezeichnung = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, k.getBezeichnung());
			stmt.setInt(2, k.getId());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		String query = "DELETE FROM kategorie WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
