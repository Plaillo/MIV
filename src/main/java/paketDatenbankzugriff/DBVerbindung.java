package paketDatenbankzugriff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Objekte zur Herstellung der DB-Verbindung werden initialisiert
 */
public class DBVerbindung {
	Connection conn = null;
	Statement stmtSQL = null;

	/**
	 * Herstellen der Datenbankverbindung.
	 *  Mithilfe des "jdbdc"-Datenbanktreibers wird die mitarbeiterDB auf localhost aufgerufen.
	 */
	public void oeffneDB() {
		try {
			System.out.println("* Treiber laden");
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println(Class.forName("com.mysql.cj.jdbc.Driver"));
			System.out.println(Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
			// com.mysql.cj.jdbc.Driver neu
			// com.mysql.jdbc.Driver deprecated
		} catch (Exception e) {
			System.err.println("Unable to load Database");
			e.printStackTrace(); 
		}
		String URL = "jdbc:mysql://localhost:3306/mitarbeiterdb";
		String USER = "root";
		String PASS = "";
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			// Initialisierung des Objekts stmtSQL mit den Verbindungsdaten des
			// Verbindungsobjekts conn
			// Mit Hilfe des Objekts stmtSQL können mit den Methoden executeQuery und
			// executeUpdate Operationen auf der Datenbank ausgeführt werden
			stmtSQL = conn.createStatement();
			System.out.println("Connection etabliert");
		} catch (SQLException exc) {
			System.out.println("SQLException: " + exc.getMessage());
			System.out.println("SQLState : " + exc.getSQLState());
			System.out.println("VendorError " + exc.getErrorCode());
		}
	}

	/**
	 * Die Datenbank wird mit dem übergebenen SQL-Befehl aktualisiert. Es kann eine:
	 * Einfügeabfrage(insert into...) 
	 * Aktualisierungsabfrage(update...)
	 * Löschabfrage(delete from...) 
	 * übergeben werden
	 */
	public boolean aendern(String pSQL) {
		try {
			stmtSQL = conn.createStatement();
			stmtSQL.executeUpdate(pSQL);
			return true;
		} catch (SQLException err) {
			System.err.println(err);
			return false;
		}
	}

	/**
	 * Die Datenbank wird mit dem übergebenden SQL-Befehl ausgewertet (abgefragt)
	 */
	public ResultSet lesen(String pSQL) {
		ResultSet rs;
		try {
			stmtSQL = conn.createStatement();
			rs = stmtSQL.executeQuery(pSQL);
			return rs;
		} catch (SQLException err) {
			System.err.println(err);
			rs = null;
			return rs;
		}
	}

	/**
	 * schließen/beenden der Datenbankverbindung
	 */
	public void schliesseDB() {
		try {
			stmtSQL.close();
			conn.close();
		} catch (SQLException err) {
			System.err.println(err);
		}
	}
}
