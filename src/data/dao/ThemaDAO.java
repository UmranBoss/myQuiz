package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import data.DBConnection;
import model.Thema;

public class ThemaDAO extends BaseDAO<Thema> {

	private final Connection conn;

	public ThemaDAO() {
		this.conn = DBConnection.getInstance().getConnection();
	}

	public boolean create(Thema t) {
		String query = "INSERT INTO thema (bezeichnung) VALUES (?)";
		try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, t.getBezeichnung());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						t.setId(generatedKeys.getInt(1));
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
	public Thema findById(int id) {
		String query = "SELECT * FROM thema WHERE id = ?";
		List<Thema> themaList = executeQuery(query, new Object[] { id }, rs -> {
			// Thema-Objekt erstellen
			Thema t = new Thema(rs.getString("bezeichnung"));
			t.setId(rs.getInt("id"));
			return t;
		});
		return themaList.isEmpty() ? null : themaList.get(0);
	}

	@Override
	public boolean update(Thema t) {
		String query = "UPDATE thmea SET bezeichnung = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, t.getBezeichnung());
			stmt.setInt(2, t.getId());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		String query = "DELETE FROM thema WHERE id = ?";
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
