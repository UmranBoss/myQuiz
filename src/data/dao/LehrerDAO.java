package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import data.DBConnection;
import model.Lehrer;

public class LehrerDAO extends BaseDAO<Lehrer> {

	private final Connection conn;

	public LehrerDAO() {
		this.conn = DBConnection.getInstance().getConnection(); // Verbindung nur EINMAL holen
	}

	@Override
	public boolean create(Lehrer l) {
		String query = "INSERT INTO lehrer (vorname, nachname, email, passwort) VALUES (?,?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, l.getVorname());
			stmt.setString(2, l.getNachname());
			stmt.setString(3, l.getEmail());
			stmt.setString(4, l.getPasswort());

			int rowsAffected = stmt.executeUpdate();
			// ID nach dem Insert auslesen
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						l.setId(generatedKeys.getInt(1)); // ID wird hier gesetzt
					}
				}
			}
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
			// Lehrer-Objekt ohne ID erstellen
			Lehrer l = new Lehrer(rs.getString("vorname"), rs.getString("nachname"), rs.getString("email"),
					rs.getString("passwort"));
			l.setId(rs.getInt("id")); // Die ID wird von der DB gesetzt
			return l;
		});
		return lehrerList.isEmpty() ? null : lehrerList.get(0);
	}

	@Override
	public boolean update(Lehrer lehrer) {
		String query = "UPDATE lehrer SET vorname = ?, nachname = ?, email = ?, passwort = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
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
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}