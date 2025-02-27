package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/bbqquiz";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	// Einzige Instanz der Verbindung
	private static DBConnection instance;
	private Connection connection;

	// Privater Konstruktor, um Instanziierungen von außen zu verhinden
	private DBConnection() {
	}

	// Singleton - Rückgabe der einzigartigen Instanz der Datenbankverbindung
	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
			// DBConnection Setup nur beim ersten Mal
			try {
				instance.connection = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("DB-Verbindung aufgebaut!");

				// Shutdown Hook Setup: Verbindung wird beim Beenden der Anwendung geschlossen
				Thread shutdownHook = new Thread(() -> {
					try {
						if (instance.connection != null && !instance.connection.isClosed()) {
							instance.connection.close();
							System.out.println("DB-Verbindung beim Beenden der Anwendung geschlossen!");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				// Der Shutdown Hook wird registriert, sodass dieser Thread beim Beenden der
				// Anwendung ausgeführt wird
				Runtime.getRuntime().addShutdownHook(shutdownHook);
			} catch (SQLException e) {
				e.addSuppressed(e);
			}
		}
		return instance;
	}

	// Gibt die Datenbankverbindung zurück
	public Connection getConnection() {
		try {
			// Falls die Verbindung geschlossen wurde, erstelle eine neue
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("Neue DB-Verbindung erstellt!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
