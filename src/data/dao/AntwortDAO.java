package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import data.DBConnection;
import model.Antwort;
import model.Frage;
import model.Fragetyp;

public class AntwortDAO extends BaseDAO<Antwort> {

	private FrageDAO frageDAO;
	private final Connection conn;

	public AntwortDAO() {
		this.conn = DBConnection.getInstance().getConnection();
	}

	// Setter für FrageDAO
	public void setFrageDAO(FrageDAO frageDAO) {
		this.frageDAO = frageDAO;
	}

	@Override
	public boolean create(Antwort a) {
		String query = "INSERT INTO antwort (antworttext, punktzahl, richtigkeit, frage_id) VALUES (?,?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, a.getAntworttext());
			stmt.setInt(2, a.getPunktzahl());
			stmt.setBoolean(3, a.isRichtigkeit());
			stmt.setInt(4, a.getFrage().getId());

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						a.setId(generatedKeys.getInt(1));
					}
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Antwort findById(int id) {
		String query = "SELECT * FROM antwort WHERE id = ?";
		return executeQuery(query, new Object[] { id }, rs -> mapResultSetToAntwort(rs)).stream().findFirst()
				.orElse(null);
	}

	public List<Antwort> findByFrageId(int frageId) {
		String query = "SELECT * FROM antwort WHERE frage_id = ?";
		return executeQuery(query, new Object[] { frageId }, rs -> mapResultSetToAntwort(rs));
	}

//	private Antwort mapResultSetToAntwort(ResultSet rs) throws SQLException {
//		// Antwort-Objekt erstellen
//		Antwort a = new Antwort(rs.getString("antworttext"), rs.getInt("punktzahl"), rs.getBoolean("richtigkeit"),
//				frageDAO.findById(rs.getInt("frage_id")));
//		a.setId(rs.getInt("id"));
//		return a;
//	}

	private Antwort mapResultSetToAntwort(ResultSet rs) throws SQLException {
		int frageId = rs.getInt("frage_id");
		// Erstelle ein minimal befülltes Frage-Objekt mit Dummy-Werten, da kein
		// Default-Konstruktor existiert.
		Frage frage = new Frage("", Fragetyp.EINZELWAHL, 0, null);
		frage.setId(frageId);

		Antwort a = new Antwort(rs.getString("antworttext"), rs.getInt("punktzahl"), rs.getBoolean("richtigkeit"),
				frage);
		a.setId(rs.getInt("id"));
		return a;
	}

	@Override
	public boolean update(Antwort a) {
		String query = "UPDATE antwort SET antworttext = ?, punktzahl = ?, richtigkeit = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, a.getAntworttext());
			stmt.setInt(2, a.getPunktzahl());
			stmt.setBoolean(3, a.isRichtigkeit());
			stmt.setInt(4, a.getId());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		String query = "DELETE FROM antwort WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
