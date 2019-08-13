package paketDatenbankzugriff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import paketFachklassen.Mitarbeiter;

public class DBZugriff_Mitarbeiter {

	Mitarbeiter meinMitarbeiter;
	Mitarbeiter meinArbeitnehmer;
	DBVerbindung verbindung;
	ArrayList<Mitarbeiter> mitarbeiterliste;
	String mSQL;
	boolean ok;

	public DBZugriff_Mitarbeiter() {
		verbindung = new DBVerbindung();
		meinMitarbeiter = new Mitarbeiter();
		mitarbeiterliste = new ArrayList<Mitarbeiter>();
	}

	/**
	 * Verbindung zur Datenbank. Fügt die Daten, die in der Methode
	 * anlegenMitarbeiter() erfasst wurden, der Datenbank hinzu.
	 */
	public boolean erfasseMitarbeiter(Mitarbeiter neuerMitarbeiter) {
		try {
			mSQL = "INSERT INTO mitarbeiter (mitnr, name, vorname, gehalt, uID) ";
			mSQL += "VALUES ('" + neuerMitarbeiter.getMitarbeiternummer() + "','";
			mSQL += neuerMitarbeiter.getName() + "','" + neuerMitarbeiter.getVorname() + "','";
			mSQL += neuerMitarbeiter.getGehalt() + "','" + neuerMitarbeiter.getUID() + "')";
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
	 * Verbindung zur Datenbank. Ändert die Daten in der Datenbank, die in der
	 * Methode aendernMitarbeiter() erfasst wurden.
	 */
	public boolean aendereMitarbeiter() {
		try {
			mSQL = "UPDATE mitarbeiter SET name = '" + meinMitarbeiter.getName();
			mSQL += "',vorname = '" + meinMitarbeiter.getVorname() + "',gehalt = '";
			mSQL += meinMitarbeiter.getGehalt() + "',uID = '" + meinMitarbeiter.getUID();
			mSQL += "' WHERE mitNr = '" + meinMitarbeiter.getMitarbeiternummer() + "';";
			verbindung.oeffneDB();
			verbindung.aendern(mSQL);
			System.out.println(mSQL);
			ok = true;
		} catch (Exception e) {
			ok = false;
		}
		verbindung.schliesseDB();
		return ok;
	}

	/**
	 * Verbindung zur Datenbank. Löscht die Daten in der Datenbank, die in der
	 * Methode loeschenMitarbeiter() erfasst wurden.
	 */
	public boolean loescheMitarbeiter() {
		try {
			// SQL-Anweisung zum Löschen des Artikels mit der eingegebenen Artikelnummer
			mSQL = "DELETE FROM Mitarbeiter WHERE mitNr ='" + meinMitarbeiter.getMitarbeiternummer() + "';";
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

	// TODO: Wenn man das Feld leert oder eine nicht-existierende MitNr eingegeben
	// hat, lässt sich kein Mitarbeiter mehr suchen
	/**
	 * Verbindung zur Datenbank. Sucht die gesuchten Daten aus der DB, fügt sie dem
	 * Objekt meinMitarbeiter hinzu, die in der Funktion suchenMitarbeiter()
	 * wiederverwendet werden
	 */
	public Mitarbeiter sucheMitarbeiter(String pMitnr) {
		ResultSet rsM;
		mSQL = "SELECT * FROM mitarbeiter ";
		mSQL = mSQL + "WHERE mitNr = '" + pMitnr + "';";
		verbindung.oeffneDB();
		rsM = verbindung.lesen(mSQL);
		try {
			rsM.next();
			meinMitarbeiter.setMitarbeiternummer(rsM.getInt("mitNr"));
			meinMitarbeiter.setName(rsM.getString("name"));
			meinMitarbeiter.setVorname(rsM.getString("vorname"));
			meinMitarbeiter.setGehalt(rsM.getDouble("gehalt"));
			meinMitarbeiter.setUID(rsM.getInt("uID"));
		} catch (SQLException err) {
			meinMitarbeiter = null;
		}
		verbindung.schliesseDB();
		return meinMitarbeiter;
	}

	public ArrayList<Mitarbeiter> sucheArbeitnehmer(String uID) {
		ResultSet rsM;
		mSQL = "SELECT * FROM mitarbeiter WHERE uID = " + uID;
		System.out.println("---------------------------" + mSQL);
		verbindung.oeffneDB();
		rsM = verbindung.lesen(mSQL);
		try {
			while (rsM.next()) {
				meinArbeitnehmer = new Mitarbeiter();
				meinArbeitnehmer.setMitarbeiternummer(rsM.getInt("mitNr"));
				meinArbeitnehmer.setName(rsM.getString("name"));
				meinArbeitnehmer.setVorname(rsM.getString("vorname"));
				meinArbeitnehmer.setGehalt(rsM.getDouble("gehalt"));
				meinArbeitnehmer.setUID(rsM.getInt("uID"));
				mitarbeiterliste.add(meinArbeitnehmer);
			}
		} catch (SQLException err) {
			System.out.println("keine Mitarbeiter gefunden");
		}
		verbindung.schliesseDB();
		return mitarbeiterliste;

	}

	public Mitarbeiter listeMitarbeiter() {
		ResultSet rsM;
		mSQL = "SELECT * FROM mitarbeiter ";
		verbindung.oeffneDB();
		rsM = verbindung.lesen(mSQL);
		try {
			rsM.next();
			meinMitarbeiter.setMitarbeiternummer(rsM.getInt("mitNr"));
			meinMitarbeiter.setName(rsM.getString("name"));
			meinMitarbeiter.setVorname(rsM.getString("vorname"));
			meinMitarbeiter.setGehalt(rsM.getDouble("gehalt"));
			meinMitarbeiter.setUID(rsM.getInt("uID"));
		} catch (SQLException err) {
			meinMitarbeiter = null;
		}
		return meinMitarbeiter;
	}

	public ArrayList<Mitarbeiter> mitarbeiterliste_db() {
		ResultSet rsM; // (2)
		mSQL = "SELECT * FROM mitarbeiter"; // (3)
		verbindung.oeffneDB(); // (4)
		rsM = verbindung.lesen(mSQL); // (5)
		try { // (6)
			while (rsM.next()) { // (7)
				meinMitarbeiter = new Mitarbeiter(); // (8)
				meinMitarbeiter.setMitarbeiternummer(rsM.getInt("mitNr")); // (9)
				meinMitarbeiter.setName(rsM.getString("name"));
				meinMitarbeiter.setVorname(rsM.getString("vorname"));
				meinMitarbeiter.setGehalt(rsM.getDouble("gehalt"));
				meinMitarbeiter.setUID(rsM.getInt("uID"));
				mitarbeiterliste.add(meinMitarbeiter); // (10)
			}
		} catch (SQLException e) { // (11)
			System.out.println("keine Mitarbeiter gefunden");
		}
		verbindung.schliesseDB();
		return mitarbeiterliste;
	}

}
