package bl;

import data.dao.QuizDAO;
import model.Quiz;

import java.util.Optional;

public class QuizService {
	private final QuizDAO quizDAO;

	public QuizService(QuizDAO quizDAO) {
		this.quizDAO = quizDAO;
	}

	/**
	 * Speichert ein neues Quiz in der DB
	 * 
	 * @param quiz Das zu speichernde Quiz
	 * @return true, wenn erfolgreich gespeichert, sonst false
	 */
	public boolean saveQuiz(Quiz quiz) {
		if (quiz == null) {
			throw new IllegalArgumentException("Quiz darf nicht null sein!");
		}
		return quizDAO.create(quiz);
	}

	/**
	 * Lädt ein Quiz anhand seiner ID
	 * 
	 * @param id Die ID des Quizzes
	 * @return Das Quiz-Objekt oder null, falls nicht gefunden
	 */
	public Optional<Quiz> getQuizById(int id) {
		return Optional.ofNullable(quizDAO.findById(id));
	}

	/**
	 * Aktualisiert ein bestehendes Quiz.
	 * 
	 * @param quiz Das zu aktualisierende Quiz
	 * @return true, wenn das Update erfolgreich war, sonst false
	 */
	public boolean updateQuiz(Quiz quiz) {
		if (quiz == null || quiz.getId() == 0) {
			throw new IllegalArgumentException("Ungültiges Quiz für Update!");
		}
		return quizDAO.update(quiz);
	}

	/**
	 * Löscht ein Quiz anhand seiner ID.
	 * 
	 * @param id Die ID des Quizzes
	 * @return true, wenn das Quiz gelöscht wurde, sonst false
	 */
	public boolean deleteQuiz(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("Ungültige Quiz-ID für Löschung!");
		}
		return quizDAO.delete(id);
	}
}
