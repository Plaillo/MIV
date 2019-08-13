package paketFachklassen;

public class Unternehmen {

	private int unternehmensID;
	private String untName;
	private String stadt;
	private String strasse;
	private int hausNr;
	private int plz;
	
	public Unternehmen() {
	}
	
	public int getUnternehmensID() {
		return unternehmensID;
	}

	public void setUnternehmensID(int unternehmensID) {
		this.unternehmensID = unternehmensID;
	}

	public String getUntName() {
		return untName;
	}

	public void setUntName(String untName) {
		this.untName = untName;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getHausNr() {
		return hausNr;
	}

	public void setHausNr(int hausNr) {
		this.hausNr = hausNr;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	
}
