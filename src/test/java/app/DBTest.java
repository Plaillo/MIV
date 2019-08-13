package app;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;

import paketDatenbankzugriff.DBVerbindung;

public class DBTest {

	@Test
	public void testName() throws Exception {
		DBVerbindung verbindung = new DBVerbindung();
		verbindung.oeffneDB();
		
		ResultSet rs = verbindung.lesen("SELECT * FROM Mitarbeiter");
		while(rs.next()) {
			System.out.println(rs.getString("Mitnr"));
		}
		
		verbindung.schliesseDB();
	}
	
}
