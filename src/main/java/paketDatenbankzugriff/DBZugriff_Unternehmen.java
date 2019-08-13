package paketDatenbankzugriff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import paketFachklassen.Mitarbeiter;
import paketFachklassen.Unternehmen;

public class DBZugriff_Unternehmen {

	Mitarbeiter meinMitarbeiter;
	Unternehmen meinUnternehmen;
	DBVerbindung verbindung;
	String mSQL;
	boolean ok;
	ArrayList<Unternehmen> unternehmensliste;
	
	
	public DBZugriff_Unternehmen() {
		verbindung = new DBVerbindung();
		meinUnternehmen = new Unternehmen();
		meinMitarbeiter = new Mitarbeiter();
		unternehmensliste = new ArrayList<Unternehmen>();
	}
	
	public Unternehmen sucheUnternehmen(String pName) {
		ResultSet rsM;
		mSQL = "SELECT * FROM unternehmen ";
		mSQL = mSQL + "WHERE unternehmensID = '" + pName + "';";
		verbindung.oeffneDB();
		rsM = verbindung.lesen(mSQL);
		try {
			rsM.next();
			meinUnternehmen.setUnternehmensID(rsM.getInt("unternehmensID"));
			meinUnternehmen.setUntName(rsM.getString("name"));
			meinUnternehmen.setStadt(rsM.getString("stadt"));
			meinUnternehmen.setStrasse(rsM.getString("strasse"));
			meinUnternehmen.setHausNr(rsM.getInt("hausNr"));
			meinUnternehmen.setPlz(rsM.getInt("plz"));
		} catch (SQLException err) {
			meinUnternehmen = null;
		}
		verbindung.schliesseDB();
		return meinUnternehmen;
	}
	
	/**
	 *  Verbindung zur Datenbank. Fügt die Daten, die in der Methode anlegenUnternehmen() erfasst wurden, der Datenbank hinzu.
	 */
	public boolean erfasseUnternehmen(Unternehmen neuesUnternehmen) {
		try {
			mSQL = "INSERT INTO unternehmen (name, stadt, strasse, hausNr, plz) ";
			mSQL += "VALUES( '" + neuesUnternehmen.getUntName() + "','";
			mSQL += neuesUnternehmen.getStadt() + "','" + neuesUnternehmen.getStrasse() + "','";
			mSQL += neuesUnternehmen.getHausNr() + "','" + neuesUnternehmen.getPlz() + "')";
			verbindung.oeffneDB();
			ok = verbindung.aendern(mSQL);
			verbindung.schliesseDB();
		} catch (Exception e) {
			System.out.println(e);
			ok = false;
		}
		return ok;
	}

	/**
	 * Verbindung zur Datenbank. Ändert die Daten in der Datenbank, die in der Methode aendernUnternehmen() erfasst wurden.
	 */
	public boolean aendereUnternehmen() {
		try {
			mSQL = "UPDATE unternehmen SET name = '" + meinUnternehmen.getUntName();
			mSQL += "',stadt = '" + meinUnternehmen.getStadt() + "',strasse = '";
			mSQL += meinUnternehmen.getStrasse() + "',hausNr = '" + meinUnternehmen.getHausNr();
			mSQL += "',plz = '" + meinUnternehmen.getPlz();
			mSQL += "' WHERE name = '" + meinUnternehmen.getUntName() + "';" ;
			verbindung.oeffneDB();
			verbindung.aendern(mSQL);
			ok = true;
		} catch (Exception e) {
			ok = false;
		}
		verbindung.schliesseDB();
		return ok;
	}

	/**
	 * Verbindung zur Datenbank. Löscht die Daten in der Datenbank, die in der Methode loeschenUnternehmen() erfasst wurden.
	 */
	public boolean loescheUnternehmen() {
		try {
			// SQL-Anweisung zum Löschen des Artikels mit der eingegebenen Artikelnummer
			mSQL = "DELETE FROM Unternehmen WHERE name ='" + meinUnternehmen.getUntName() + "';";
			verbindung.oeffneDB(); // (1) Datenbankverbindung öffnen
			verbindung.aendern(mSQL); // (2) Datenbank mit der Löschanweisung aktualisieren
			ok = true;
			System.out.println(ok);
		} catch (Exception e) {
			ok = false;
		}
		verbindung.schliesseDB(); // (3) Datenbankverbindung schließen
		return ok; // (4)
	}
	
	public Mitarbeiter listeMitarbeiter() {
		ResultSet rsM;
		mSQL = "SELECT * FROM mitarbeiter ";
		verbindung.oeffneDB();
		rsM = verbindung.lesen(mSQL);
		try {
			rsM.next();
			meinMitarbeiter.setMitarbeiternummer(rsM.getInt("mitNr"));
		} catch (SQLException err) {
			meinMitarbeiter = null;
		}
		return meinMitarbeiter;
	}
	
	public ArrayList<Unternehmen> unternehmensliste_db() {
		ResultSet rsM; // (2)
		mSQL = "SELECT * FROM unternehmen"; // (3)
		verbindung.oeffneDB(); // (4)
		rsM = verbindung.lesen(mSQL); // (5)
		try { // (6)
			while (rsM.next()) { // (7)
				meinUnternehmen = new Unternehmen(); // (8)
				meinUnternehmen.setUnternehmensID(rsM.getInt("unternehmensID")); // (9)
				meinUnternehmen.setUntName(rsM.getString("name"));
				meinUnternehmen.setStadt(rsM.getString("stadt"));
				meinUnternehmen.setStrasse(rsM.getString("strasse"));
				meinUnternehmen.setHausNr(rsM.getInt("hausNr"));
				meinUnternehmen.setPlz(rsM.getInt("plz"));
				unternehmensliste.add(meinUnternehmen); // (10)
			}
		} catch (SQLException e) { // (11)
			System.out.println("keine Unternehmen gefunden");
		}
		verbindung.schliesseDB();
		return unternehmensliste;
	}
	
	
}
