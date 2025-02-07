package data.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import data.DBConnection;
import model.Thema;

public class ThemaDAO extends BaseDAO<Thema> {
	public boolean create(Thema t) {
		String query = "INSERT INTO thema (id, bezeichnung) VALUES (?,?)";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setInt(1, t.getId());
			stmt.setString(2, t.getBezeichnung());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Thema findById(int id) {
		String query = "SELECT * FROM thema WHERE = ?";
		List<Thema> themaList = executeQuery(query, new Object[] { id }, rs -> {
			return new Thema(rs.getInt("id"), rs.getString("bezeichnung"));
		});
		return themaList.isEmpty() ? null : themaList.get(0);
	}

	@Override
	public boolean update(Thema t) {
		String query = "UPDATE thmea SET bezeichnung = ? WHERE = ?";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
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
		String query = "DELETE FROM thema WHERE = ?";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
