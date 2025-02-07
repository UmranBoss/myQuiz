package data.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import data.DBConnection;
import model.Kategorie;

public class KategorieDAO extends BaseDAO<Kategorie> {

	@Override
	public boolean create(Kategorie k) {
		String query = "INSERT INTO kategorie (id, bezeichnung) VALUES (?,?)";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setInt(1, k.getId());
			stmt.setString(2, k.getBezeichnung());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Kategorie findById(int id) {
		String query = "SELECT * FROM kategorie WHERE = ?";
		List<Kategorie> kategorieList = executeQuery(query, new Object[] { id }, rs -> {
			return new Kategorie(rs.getInt("id"), rs.getString("bezeichnung"));
		});
		return kategorieList.isEmpty() ? null : kategorieList.get(0);
	}

	@Override
	public boolean update(Kategorie k) {
		String query = "UPDATE kategorie SET bezeichnung = ? WHERE = ?";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
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
		String query = "DELETE FROM kategorie WHERE = ?";
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
