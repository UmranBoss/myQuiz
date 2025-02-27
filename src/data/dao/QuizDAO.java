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
import model.Kategorie;
import model.Lehrer;
import model.Quiz;
import model.Thema;

public class QuizDAO extends BaseDAO<Quiz> {

	private final LehrerDAO lehrerDAO;
	private final KategorieDAO kategorieDAO;
	private final ThemaDAO themaDAO;
	private final FrageDAO frageDAO;
	private final AntwortDAO antwortDAO;
	private final Connection conn;

	public QuizDAO(LehrerDAO lehrerDAO, KategorieDAO kategorieDAO, ThemaDAO themaDAO, FrageDAO frageDAO,
			AntwortDAO antwortDAO) {
		super();
		this.conn = DBConnection.getInstance().getConnection();
		this.lehrerDAO = lehrerDAO;
		this.kategorieDAO = kategorieDAO;
		this.themaDAO = themaDAO;
		this.frageDAO = frageDAO;
		this.antwortDAO = antwortDAO;
	}

	@Override
	public boolean create(Quiz q) {
		System.out.println("🛠 Anzahl der Fragen vor dem Speichern: "
				+ (q.getFragenListe() != null ? q.getFragenListe().size() : "NULL"));

		String query = "INSERT INTO quiz (titel, lehrer_id, kategorie_id, thema_id) VALUES (?,?,?,?)";
		String frageQuery = "INSERT INTO frage (fragetext, fragetyp, maxPunktzahl, quiz_Id) VALUES (?,?,?,?)";
		try {
			conn.setAutoCommit(false); // Transaktion manuell steuern
			// 1. Quiz speichern
			int quizId = -1;
			try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, q.getTitel());
				stmt.setInt(2, q.getLehrer().getId());
				stmt.setInt(3, q.getKategorie().getId());
				stmt.setInt(4, q.getThema().getId());
				stmt.executeUpdate();

				// Quiz-ID abrufen
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						quizId = generatedKeys.getInt(1);
						q.setId(quizId); // Quiz-Objekt die neue ID zuweisen
						System.out.println("✅ Quiz erfolgreich gespeichert mit ID: " + quizId);
					} else {
						throw new SQLException("Quiz-ID konnte nicht generiert werden!");
					}
				}
			}
			// Sicherstellen, dass die ID nicht -1 ist
			if (quizId == -1) {
				throw new SQLException("❌ Quiz-ID wurde nicht korrekt gesetzt!");
			}

			// 2. Frage speichern (mit der neuen Quiz-ID)
			System.out.println("🧐 Anzahl der Fragen im Quiz: " + q.getFragenListe().size());

			for (Frage f : q.getFragenListe()) {
				System.out.println("📌 Frage-Objekt vorhanden: " + f.getFragetext() + " | Quiz-ID: "
						+ (f.getQuiz() != null ? f.getQuiz().getId() : "NULL"));
				f.setQuiz(q);
				System.out.println("🔍 Frage wird gespeichert: " + f.getFragetext());
				boolean erfolg = frageDAO.create(f);
				if (!erfolg || f.getId() == 0) { // 🛑 Fehler abfangen
					throw new SQLException("❌ Fehler: Frage wurde nicht korrekt gespeichert! Frage-ID: " + f.getId());
				}

				System.out.println("✅ Wurde die Frage gespeichert? " + erfolg);

			}
//			try (PreparedStatement stmt = conn.prepareStatement(frageQuery)) {
//				for (Frage f : q.getFragenListe()) {
//					stmt.setString(1, f.getFragetext());
//					stmt.setString(2, f.getFragetyp().name()); // Enum in String umwandeln
//					stmt.setInt(3, f.getMaxPunktzahl());
//					stmt.setInt(4, quizId); // generierte Quiz-ID setzen
//					stmt.addBatch();
//				}
//				/*
//				 * Alle Fragen in einem Schritt speichern Batch-Processing: Mehrere SQL-Befehle
//				 * sammeln & in einem einzigen Schritt ausführen
//				 */
//				stmt.executeBatch();
//			}
			// 3. Antworten für jede Frage speichern
			for (Frage f : q.getFragenListe()) {
				System.out.println("📌 Frage wird bearbeitet: " + f.getFragetext());
				for (Antwort antwort : f.getAntworten()) {
					antwort.setFrage(f); // Antwort mit Frage verknüpfen
					antwortDAO.create(antwort); // Antwort speichern
				}
			}

			conn.commit(); // Transaktion schließen
			// 📌 Quiz mit gespeicherten Fragen neu laden
			Quiz gespeichertesQuiz = findById(quizId);
			System.out.println("🔄 Quiz nach Speichern neu geladen: " + gespeichertesQuiz.getFragenListe().size()
					+ " Fragen gefunden.");
			return true;
		} catch (SQLException e) {
			System.out.println("❌ Fehler beim Speichern der Fragen: " + e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback(); // Bei Fehlern: Rollback
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public Quiz findById(int id) {
		String query = "SELECT * FROM quiz WHERE id = ?";
		return executeQuery(query, new Object[] { id }, rs -> {
			int quizId = rs.getInt("id");
			String titel = rs.getString("titel");
			Lehrer l = lehrerDAO.findById(rs.getInt("lehrer_id"));
			Kategorie k = kategorieDAO.findById(rs.getInt("kategorie_id"));
			Thema t = themaDAO.findById(rs.getInt("thema_Id"));

			// Fragen des Quiz laden
			List<Frage> fragenListe = frageDAO.findQuizById(quizId);

			// Quiz-Objekt erstellen und ID setzen
			Quiz quiz = new Quiz(titel, l, k, t, fragenListe);
			quiz.setId(quizId); // WICHTIG! Setzt die ID des Quiz

			// Setze in jeder Frage den Verweis auf das übergeordnete Quiz
			for (Frage f : fragenListe) {
				f.setQuiz(quiz);
			}

			// 🔍 Debugging-Logs
			System.out.println("✅ Quiz geladen mit ID: " + quiz.getId());
			System.out.println("🧐 Anzahl der Fragen im Quiz: " + quiz.getFragenListe().size());

			return quiz;
		}).stream().findFirst().orElse(null);
	}

	public List<Quiz> findAll() {
		String query = "SELECT * FROM quiz";
		List<Quiz> quizzes = executeQuery(query, new Object[] {}, rs -> {
			int quizId = rs.getInt("id");
			String titel = rs.getString("titel");
			Lehrer l = lehrerDAO.findById(rs.getInt("lehrer_id"));
			Kategorie k = kategorieDAO.findById(rs.getInt("kategorie_id"));
			Thema t = themaDAO.findById(rs.getInt("thema_Id"));

			List<Frage> fragenListe = frageDAO.findQuizById(quizId);
			// Setze auch hier in jeder Frage den Verweis zum Quiz später
			Quiz quiz = new Quiz(titel, l, k, t, fragenListe);
			quiz.setId(quizId);
			// Antworten für jede Frage laden:
			for (Frage f : fragenListe) {
				f.setAntworten(antwortDAO.findByFrageId(f.getId()));
			}

			return quiz;
		});
		return quizzes;
	}

	@Override
	public boolean update(Quiz quiz) {
		String query = "UPDATE quiz SET titel = ?, lehrer_id = ?, kategorie_id = ?, thema_id = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, quiz.getTitel());
			stmt.setInt(2, quiz.getLehrer().getId());
			stmt.setInt(3, quiz.getKategorie().getId());
			stmt.setInt(4, quiz.getThema().getId());
			stmt.setInt(5, quiz.getId());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				updateFragenListe(quiz); // Fragenliste synchronisieren
			}
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void updateFragenListe(Quiz quiz) {
		List<Integer> existingFragenIds = frageDAO.getFragenIdsByQuiz(quiz.getId());

		for (Frage f : quiz.getFragenListe()) {
			if (f.getId() == 0) {
				frageDAO.create(f);
			} else {
				frageDAO.create(f);
				existingFragenIds.remove(Integer.valueOf(f.getId()));
			}
		}
		// Verbleibende IDs in "existingFragenIds" sind gelöschte Fragen -> Löschen
		for (int frageId : existingFragenIds) {
			frageDAO.delete(frageId);
		}
	}

	@Override
	public boolean delete(int id) {
		try {
			frageDAO.delete(id); // erst alle Fragen löschen
			String query = "DELETE FROM quiz WHERE id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setInt(1, id);
				int rowsAffected = stmt.executeUpdate();
				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}