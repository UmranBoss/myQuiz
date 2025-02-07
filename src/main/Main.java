package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.DBConnection;

public class Main {

	public static void main(String[] args) {
		// Singleton-Instanz holen
		DBConnection dbConnection = DBConnection.getInstance();

		// Verbindung zur DB erhalten
		Connection connection = dbConnection.getConnection();

		// Test: SQL-Abfrage für die Anzahl der Tabellen, um den Verbindungsaufbau zu
		// prüfen
		String tableCountQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'bbqquiz'";
		try (PreparedStatement stmt = connection.prepareStatement(tableCountQuery)) {
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int tableCount = rs.getInt(1); // Die Anzahl der Tabellen wird in der ersten Spalte zurückgegeben
				System.out.println("Anzahl der Tabellen in der DB: " + tableCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
