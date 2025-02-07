package data.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import data.DBConnection;
import model.Lehrer;

public class LehrerDAO extends BaseDAO<Lehrer> {

	@Override
	public boolean create(Lehrer l) {
		String query = "INSERT INTO lehrer (id, vorname, nachname, email, passwort) VALUES (?,?,?,?,?)";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setInt(1, l.getId());
			stmt.setString(2, l.getVorname());
			stmt.setString(3, l.getNachname());
			stmt.setString(4, l.getEmail());
			stmt.setString(5, l.getPasswort());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Rückgabe true, wenn die Operation erfolgreich war
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Lehrer findById(int id) {
		String query = "SELECT * FROM lehrer WHERE id = ?";
		List<Lehrer> lehrerList = executeQuery(query, new Object[] { id }, rs -> {
			return new Lehrer(rs.getInt("id"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("email"),
					rs.getString("passwort"));
		});
		return lehrerList.isEmpty() ? null : lehrerList.get(0);
	}

	@Override
	public boolean update(Lehrer lehrer) {
		String query = "UPDATE lehrer SET vorname = ?, nachname = ?, email = ?, passwort = ? WHERE id = ?";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setString(1, lehrer.getVorname());
			stmt.setString(2, lehrer.getNachname());
			stmt.setString(3, lehrer.getEmail());
			stmt.setString(4, lehrer.getPasswort());
			stmt.setInt(5, lehrer.getId());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		String query = "DELETE FROM lehrer WHERE id = ?";
		try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}