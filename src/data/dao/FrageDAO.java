package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.DBConnection;
import model.Antwort;
import model.Frage;
import model.Fragetyp;

public class FrageDAO extends BaseDAO<Frage> {

	private QuizDAO quizDAO; // Um Quiz-Objekte zu holen; final entfernt, damit der QuizDAO nachträglich
								// gesetzt werden kann
	private final AntwortDAO antwortDAO;
	private final Connection conn;

	public FrageDAO(QuizDAO quizDAO, AntwortDAO antwortDAO) {
		this.quizDAO = quizDAO; // kann vorläufig null sein
		this.antwortDAO = antwortDAO;
		this.conn = DBConnection.getInstance().getConnection();
	}

	public FrageDAO(AntwortDAO antwortDAO) {

		this.antwortDAO = antwortDAO;
		this.conn = DBConnection.getInstance().getConnection();
	}

	// Setter für QuizDAO
	public void setQuizDAO(QuizDAO quizDAO) {
		this.quizDAO = quizDAO;
	}

	@Override
	public boolean create(Frage f) {
		System.out.println("Quiz-ID: " + f.getQuiz());
		String query = "INSERT INTO frage (fragetext, fragetyp, maxPunktzahl, quiz_id) VALUES (?,?,?,?)";
		try {
			conn.setAutoCommit(false); // Transaktion starten

			try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, f.getFragetext());
				stmt.setString(2, f.getFragetyp().name());
				stmt.setInt(3, f.getMaxPunktzahl());

				System.out.println("📌 Quiz-ID der Frage: " + (f.getQuiz() != null ? f.getQuiz().getId() : "NULL"));

				if (f.getQuiz() == null) { // Prüfe, ob Quiz vorhanden ist
					System.out.println("⚠️ Fehler: Die Frage hat kein zugeordnetes Quiz!");
					return false;
				}
				stmt.setInt(4, f.getQuiz().getId());
				System.out.println("🛠 DEBUG: Frage wird jetzt in die DB eingefügt!");
				System.out.println("📌 Fragetext: " + f.getFragetext());
				System.out.println("📌 Fragetyp: " + f.getFragetyp().name());
				System.out.println("📌 MaxPunktzahl: " + f.getMaxPunktzahl());
				System.out.println("📌 Quiz-ID: " + (f.getQuiz() != null ? f.getQuiz().getId() : "NULL"));

				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("✅ Frage erfolgreich gespeichert mit ID: " + f.getId());
					try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							f.setId(generatedKeys.getInt(1)); // ID der neuen Frage setzen
							System.out.println("✅ Frage erfolgreich gespeichert mit ID: " + f.getId());
							// 📌 Frage zur `fragenListe` im Quiz hinzufügen!
							f.getQuiz().getFragenListe().add(f);
							System.out.println("📌 Frage zum Quiz hinzugefügt! Neue Anzahl: "
									+ f.getQuiz().getFragenListe().size());
						}
					}
				} else {
					System.out.println("⚠️ Fehler: Frage wurde nicht gespeichert!");
					conn.rollback(); // Falls keine Zeilen eingefügt wurden, Transaktion abbrechen
					return false;
				}

				// Speichern der Antworten für diese Frage
				for (Antwort antwort : f.getAntworten()) {
					antwort.setFrage(f);
					if (!antwortDAO.create(antwort)) { // Falls Antwortspeicherung fehlschlägt
						conn.rollback(); // Transaktion rückgängig machen
						System.out.println("⚠️ Fehler: Antworten konnten nicht gespeichert werden!");
						return false;
					}
				}

				conn.commit(); // Erfolgreich -> Transaktion abschließen
				return true;

			} catch (SQLException e) {
				conn.rollback(); // Falls ein Fehler passiert, wird alles rückgängig gemacht
				e.printStackTrace();
				return false;
			} finally {
				conn.setAutoCommit(true); // Automatische Transaktionen wieder aktivieren
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// READ: Frage anhand der ID abrufen
	// Gibt eine Liste von Frage-Objekten zurück
	// Notwendig, wenn ich ganze Fragen eines Quiz benötige
	@Override
	public Frage findById(int id) {
		String query = "SELECT * FROM frage WHERE id = ?";
		Frage frage = executeQuery(query, new Object[] { id }, rs -> mapResultSetToFrage(rs)).stream().findFirst()
				.orElse(null);
		// Laden der Antworten für diese Frage
		if (frage != null) {
			frage.setAntworten(antwortDAO.findByFrageId(id));
		}
		return frage;
	}

	// READ: ALle Fragen zu einem Quiz abrufen
	public List<Frage> findQuizById(int quizId) {
		System.out.println("🔍 Suche alle Fragen für eine Quiz-ID: " + quizId);
		String query = "SELECT * FROM frage WHERE quiz_id = ?";
		// return executeQuery(query, new Object[] { quizId }, rs ->
		// mapResultSetToFrage(rs));
		List<Frage> fragenListe = executeQuery(query, new Object[] { quizId }, rs -> mapResultSetToFrage(rs));
		// Für jede Frage auch die Antworten laden:
		for (Frage f : fragenListe) {
			f.setAntworten(antwortDAO.findByFrageId(f.getId()));
		}
		return fragenListe;
	}

	// Nur IDs abrufen (gibt nur die IDs der Fragen zurück)
	// Notwendig, wenn ich nur die IDs für Vergleiche o. zum Löschen bentöge,
	// ohne die kompletten Frage-Objekte aus der DB zu laden.
	public List<Integer> getFragenIdsByQuiz(int quizId) {
		System.out.println("🔍 Suche Fragen aller Quiz-ID: " + quizId);
		List<Integer> fragenIds = new ArrayList<>();
		String query = "SELECT id FROM frage WHERE quiz_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, quizId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					fragenIds.add(rs.getInt("id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fragenIds;
	}

	// Hilftsmethode: ResultSet zu Frage-Objekt mappen
	private Frage mapResultSetToFrage(ResultSet rs) throws SQLException {
		// Frage-Objekt erstellen
		Frage f = new Frage(rs.getString("fragetext"), Fragetyp.valueOf(rs.getString("fragetyp").toUpperCase()),
				rs.getInt("maxPunktzahl"), null); // später wird das Quiz-Objekt gesetzt
		f.setId(rs.getInt("id"));
		return f;
	}

	@Override
	public boolean update(Frage f) {
		String query = "UPDATE frage SET fragetext = ?, fragetyp = ?, maxPunktzahl = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, f.getFragetext());
			stmt.setString(2, f.getFragetyp().name());
			stmt.setInt(3, f.getMaxPunktzahl());
			stmt.setInt(4, f.getId());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		String query = "DELETE FROM frage WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
