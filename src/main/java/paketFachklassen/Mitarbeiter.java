package paketFachklassen;

import com.vaadin.flow.component.textfield.TextArea;

import paketGUI.Fenster_Mitarbeiter;

public class Mitarbeiter {

	private int mitarbeiternummer;
	private String name;
	private String vorname;
	private double gehalt;
	private int uID;

	public Mitarbeiter() {
	}

	public String anzeigenMitarbeiterdaten() {
		return name;
	}
	
	public int getMitarbeiternummer() {
		return mitarbeiternummer;
	}

	public void setMitarbeiternummer(int mitarbeiternummer) {
		this.mitarbeiternummer = mitarbeiternummer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public double getGehalt() {
		return gehalt;
	}

	public void setGehalt(double gehalt) {
		this.gehalt = gehalt;
	}

	public int getUID() {
		return uID;
	}

	public void setUID(int uID) {
		this.uID = uID;
	}

}
