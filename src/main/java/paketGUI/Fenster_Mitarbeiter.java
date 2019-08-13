package paketGUI;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.ForDefaultConstructor;
import paketDatenbankzugriff.DBZugriff_Mitarbeiter;
import paketDatenbankzugriff.DBZugriff_Unternehmen;
import paketDatenbankzugriff.DBVerbindung;
import paketFachklassen.Mitarbeiter;
import paketFachklassen.Unternehmen;

public class Fenster_Mitarbeiter extends Div {

	DBVerbindung verbindung;
	private Mitarbeiter aktuellerMitarbeiter;
	private DBZugriff_Mitarbeiter aktuellerZugriffMit;
	private DBZugriff_Unternehmen aktuellerZugriffUnt;
	private List<Mitarbeiter> mitarbeiterList;
	private List<Unternehmen> uidList;
	private int uid;
	VerticalLayout leftLayout;
	Dialog loeschDialog;
	Mitarbeiter meinMitarbeiter;
	Grid<Mitarbeiter> grid;
	ArrayList<String> uidArrayList;
	ArrayList<String> mitNrArrayList;
	private int mitarbeiterNr;
	private String mitarbeiterName;

	TextField txtMaNr = new TextField();
	TextField txtName = new TextField();
	TextField txtVorname = new TextField();
	TextField txtGehalt = new TextField();
	TextArea txtrAllemitarbeiter = new TextArea();
	ComboBox<String> cbUID = new ComboBox<>();
	ComboBox<String> cbMitNr = new ComboBox<>();

	private Grid<Mitarbeiter> createGrid(Collection<Mitarbeiter> mitarbeiterListe) {
		this.grid = new Grid<>();

		grid.setHeightByRows(true);
//		grid.getStyle().set("background-color", "gray");
		grid.setWidthFull();
		grid.getElement().setAttribute("theme", "compact");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setVerticalScrollingEnabled(true);

		// TODO: StringArray richtig auslesen
		grid.addSelectionListener(event -> {
			Set<Mitarbeiter> selected = event.getAllSelectedItems();
			Notification.show("will be added later");
		});

		return grid;
	}

	public Fenster_Mitarbeiter() {
		aktuellerZugriffMit = new DBZugriff_Mitarbeiter();
		aktuellerZugriffUnt = new DBZugriff_Unternehmen();
		meinMitarbeiter = new Mitarbeiter();
		uidArrayList = new ArrayList<>();
		mitNrArrayList = new ArrayList<>();
		mitarbeiterList = aktuellerZugriffMit.mitarbeiterliste_db();
		
		
		// Liste der UID für die ComboBox
		uidList = aktuellerZugriffUnt.unternehmensliste_db();
		for (int i = 0; i < uidList.size(); i++) {
			this.uid = uidList.get(i).getUnternehmensID();
			uidArrayList.add(Integer.toString(uid));
		}
		if (uidList == null) {
			leeren();
			Notification.show("keine passenden Unternehmen gefunden");
		} else {
		}

		// Liste der Mitarbeiternummern für die ComboBox
		for (int i = 0; i < mitarbeiterList.size(); i++) {
			this.mitarbeiterNr = mitarbeiterList.get(i).getMitarbeiternummer();
			this.mitarbeiterName = mitarbeiterList.get(i).getName();
			mitNrArrayList.add(Integer.toString(mitarbeiterNr) + " " + mitarbeiterName);
		}

		this.setSizeFull();

		HorizontalLayout layout = new HorizontalLayout();
//		layout.getStyle().set("background-color", "red");
		layout.setHeight("600px");
		layout.setWidth("1000px");
		layout.setSpacing(true);
		layout.setPadding(true);

		// ------------------------------ linkes Layout ------------------------------
		// //
		VerticalLayout leftLayout = new VerticalLayout();
//		leftLayout.getStyle().set("background-color", "cyan");
		leftLayout.setPadding(true);
		leftLayout.setHeightFull();
		leftLayout.setWidth("600px");

		Label headline = new Label("Mitarbeiterdaten verwalten");
		headline.getStyle().set("font-size", "24px");
		headline.getStyle().set("line-weight", "6px");
		leftLayout.add(headline);

		HorizontalLayout mitarbeiterNrLayout = new HorizontalLayout();
		Label mitNrLabel = new Label("Mitarbeiter Nummer eingeben:");
		mitNrLabel.getStyle().set("align-self", "center");
		mitNrLabel.setWidth("250px");
		txtMaNr.setWidth("275px");
		txtMaNr.setReadOnly(true);
		mitarbeiterNrLayout.add(mitNrLabel, txtMaNr);
		leftLayout.add(mitarbeiterNrLayout);

		HorizontalLayout namelayout = new HorizontalLayout();
		Label nameLabel = new Label("Name:");
		nameLabel.setWidth("20%");
		nameLabel.getStyle().set("align-self", "center");
		txtName.setWidth("40%");
		Label vornameLabel = new Label("Vorname:");
		vornameLabel.getStyle().set("align-self", "center");
		txtVorname.setWidth("40%");
		namelayout.add(nameLabel, txtName, vornameLabel, txtVorname);
		leftLayout.add(namelayout);

		HorizontalLayout gehaltLayout = new HorizontalLayout();
		Label gehaltLabel = new Label("Gehalt:");
		gehaltLabel.getStyle().set("align-self", "center");
		txtGehalt.setWidth("475px");
		gehaltLayout.add(gehaltLabel, txtGehalt);
		leftLayout.add(gehaltLayout);

		HorizontalLayout untLayout = new HorizontalLayout();
		Label uidLabel = new Label("UID: ");
		uidLabel.getStyle().set("align-self", "center");
		this.cbUID = new ComboBox<>();
		cbUID.setItems(uidArrayList);
		cbUID.setPlaceholder("wähle UID");
		cbUID.getStyle().set("align-self", "center");
		cbUID.addValueChangeListener(event -> Notification.show("UID gewechselt auf: " + cbUID.getValue()));
		untLayout.add(uidLabel, cbUID);
		leftLayout.add(untLayout);

		//  ------------------------------	GRID  ------------------------------	 //
		this.grid = createGrid(mitarbeiterList);
		leftLayout.add(grid);
		// ------------------------------ rechtes Layout ------------------------------	// 
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		rightLayout.setHeightFull();
		rightLayout.setWidth("350px");

		Button leerenBtn = new Button("Felder leeren", VaadinIcon.TRASH.create());
		leerenBtn.addClickListener(e -> leeren());
		leerenBtn.setWidth("250px");

		Label mitALabel = new Label("Mitarbeiter: ");
		mitALabel.getStyle().set("padding-right", "175px");

		Button anlegenBtn = new Button("anlegen", VaadinIcon.ANCHOR.create());
		anlegenBtn.setWidth("125px");
		anlegenBtn.addClickListener(e -> nextNumber());

		Button speichernBtn = new Button("Speichern", VaadinIcon.CLIPBOARD_USER.create());
		speichernBtn.setWidth("125px");
		speichernBtn.addClickListener(e -> anlegenMitarbeiter());

		HorizontalLayout anlegenNspeichernBtnLayout = new HorizontalLayout();
		anlegenNspeichernBtnLayout.add(anlegenBtn, speichernBtn);

		Label mitLabel = new Label("auswählen");
		mitLabel.getStyle().set("align-self", "center");
		
		this.cbMitNr = new ComboBox<>();
		cbMitNr.setItems(mitNrArrayList);
		cbMitNr.setValue(mitNrArrayList.get(0));
		cbMitNr.setWidth("175px");
		cbMitNr.getStyle().set("align-self", "center");
		cbMitNr.addValueChangeListener(event -> {
			if (event.getSource().isEmpty()) {
				cbMitNr.setValue(mitNrArrayList.get(0));
			} else {
				suchenMitarbeiterCB();
			}
		});

		HorizontalLayout mitNcbMitNrLayout = new HorizontalLayout();
		mitNcbMitNrLayout.add(mitLabel, cbMitNr);

		Button aendernBtn = new Button("Mitarbeiter ändern", VaadinIcon.EDIT.create());
		aendernBtn.setWidth("250px");
		aendernBtn.addClickListener(e -> aendernMitarbeiter());

		Button loeschenBtn = new Button("Mitarbeiter löschen", VaadinIcon.ERASER.create());
		loeschenBtn.setWidth("250px");
		loeschenBtn.addClickListener(e -> loeschenMitarbeiter());

		Button removeTableBtn = new Button("Aktualisieren", VaadinIcon.REFRESH.create());
		removeTableBtn.setWidth("250px");
		removeTableBtn.addClickListener(e -> onRemoveTableBtn());

		Button arbeiternehmerBtn = new Button("Arbeitnehmer finden", VaadinIcon.SEARCH.create());
		arbeiternehmerBtn.setWidth("250px");
		arbeiternehmerBtn.addClickListener(e -> onArbeiternehmerBtn());

		Button alleMitarbeiterBtn = new Button("Alle Mitarbeiter", VaadinIcon.ACADEMY_CAP.create());
		alleMitarbeiterBtn.setWidth("250px");
		alleMitarbeiterBtn.addClickListener(e -> onAlleMitarbeiterBtn());

		rightLayout.add(leerenBtn, mitALabel, anlegenNspeichernBtnLayout, mitNcbMitNrLayout, aendernBtn, loeschenBtn,
				arbeiternehmerBtn, alleMitarbeiterBtn);

		layout.add(leftLayout, rightLayout);
		this.add(layout);
	}

	/**
	 * flushed die aktuellen Daten
	 */
	private void leeren() {
		txtMaNr.clear();
		txtName.clear();
		txtVorname.clear();
		txtGehalt.clear();
		cbUID.clear();
	}

	/**
	 * Erfasst Inhalt aus der ComboBox cbMitNr und gibt ihn an die Methode
	 * sucheMitarbeiter() weiter. sucheMitarbeiter erfasst die Werte aus der
	 * Datenbank und gibt sie wieder zurück. Die ausgelesenen Werte werden an die
	 * Methode anzeigenMitarbeiterdaten() weitergeleitet.
	 */
	public void suchenMitarbeiterCB() {
		aktuellerMitarbeiter = aktuellerZugriffMit.sucheMitarbeiter(cbMitNr.getValue());

		if (aktuellerMitarbeiter == null) {
			leeren();
			Notification.show("keinen Mitarbeiter gefunden");
		} else {
			anzeigenMitarbeiterdaten();
		}
	}

	/**
	 * Benutzt die ausgelesenen Werte der Methode sucheMitarbeiter() und setzt sie
	 * in die entsprechenden Textfelder.
	 */
	private void anzeigenMitarbeiterdaten() {
		leeren();
		txtMaNr.setValue(Integer.toString(aktuellerMitarbeiter.getMitarbeiternummer()));
		txtName.setValue(aktuellerMitarbeiter.getName());
		txtVorname.setValue(aktuellerMitarbeiter.getVorname());
		txtGehalt.setValue(Double.toString(aktuellerMitarbeiter.getGehalt()));
		cbUID.setItems(Integer.toString(aktuellerMitarbeiter.getUID()));
	}

	/**
	 * legt durch den Button anlegenBtn ("Mitarbeiter anlegen") einen neuen
	 * Mitarbeiter an und fügt die Daten hinzu. die Methode erfasseMitarbeiter()
	 * oeffnet die Verbindung mit der Datenbank und fügt die Daten hinzu
	 */
	private void anlegenMitarbeiter() {
		Mitarbeiter neuerMitarbeiter = new Mitarbeiter();
		try {
			neuerMitarbeiter.setMitarbeiternummer(Integer.parseInt(txtMaNr.getValue()));
			neuerMitarbeiter.setName(txtName.getValue());
			neuerMitarbeiter.setVorname(txtVorname.getValue()); // (txtVorname.getText());
			neuerMitarbeiter.setGehalt(Double.parseDouble(txtGehalt.getValue()));
			neuerMitarbeiter.setUID(Integer.parseInt(cbUID.getValue()));
			boolean ok = aktuellerZugriffMit.erfasseMitarbeiter(neuerMitarbeiter);
			// Auswertung der Variablen ok
			if (ok == true) {
				Notification.show("Mitarbeiter erfasst");
			} else {
				Notification.show("Mitarbeiter nicht erfasst");
			}
		}
		// Abfangen von Eingabefehlern
		catch (Exception e) {
			Notification.show("Eingabefehler");
		}
	}

	/**
	 * ändert die Daten des Mitarbeiters in der Datenbank durch den Button
	 * aendernBtn ("Mitarbeiter ändern")
	 */
	private void aendernMitarbeiter() {
		// Übertragung der geänderten Daten aus den Textfeldern in das Objekt
		if(cbUID.isEmpty() == false) {
			aktuellerMitarbeiter.setUID(Integer.parseInt(cbUID.getValue().substring(0,3)));
		} 
		aktuellerMitarbeiter.setName(txtName.getValue());
		aktuellerMitarbeiter.setVorname(txtVorname.getValue());
		aktuellerMitarbeiter.setGehalt(Double.parseDouble(txtGehalt.getValue()));
		// Methode aendereMitarbeiter für das Objekt aktuellerZugriff ausführen und das
		// Ergebnis in die Variable ok (boolean) übernehmen
		boolean ok = aktuellerZugriffMit.aendereMitarbeiter();
		// Rückgabewert der methode boolean aendereMitarbeiter() auswerten
		if (ok) {
			Notification.show("Datensatz geändert!");
		} else {
			Notification.show("Datensatz nicht geändert!");
		}
	}

	/**
	 * Führt ein Entscheidungsdialog aus, ob man den Mitarbeiter wirklich löschen
	 * will. "Ja" => Mitarbeiter wird gelöscht "Nein" => Mitarbeiter wird nicht
	 * gelöscht "Abbrechen" => Dialog wird geschlossen
	 */
	private void loeschenMitarbeiter() {
		// Eingabedialogfenster
		this.loeschDialog = new Dialog();
		loeschDialog.setWidth("400px");
		loeschDialog.setHeight("50px");
		Label wirklichLoeschenLbl = new Label("Wirklich Löschen?");
		loeschDialog.add(wirklichLoeschenLbl);
		Button yesBtn = new Button("Ja", event -> mitarbeiterLoeschenAbfrage());
		Button noBtn = new Button("Nein", event -> mitarbeiterNichtLoeschenAbfrage());
		Button cancelnBtn = new Button("Abbrechen", event -> loeschDialog.close());
		loeschDialog.add(yesBtn, noBtn, cancelnBtn);
		loeschDialog.open();
	}

	/**
	 * loeschenMitarbeiter() Option: "Ja"
	 */
	public void mitarbeiterLoeschenAbfrage() {
		meinMitarbeiter.setMitarbeiternummer(Integer.parseInt(txtMaNr.getValue()));
		boolean ok = aktuellerZugriffMit.loescheMitarbeiter();
		if (ok) {
			Notification.show("Mitarbeiter gelöscht!!");
			this.loeschDialog.close();
		} else {
			Notification.show("Mitarbeiter nicht gelöscht!");
			this.loeschDialog.close();
		}
		leeren();
	}

	/**
	 * loeschenMitarbeiter() Option: "Nein"
	 */
	public void mitarbeiterNichtLoeschenAbfrage() {
		Notification.show("Mitarbeiter nicht gelöscht!");
		this.loeschDialog.close();
	}

	private void onRemoveTableBtn() {
	}

	/**
	 * Erstellt Mitarbeiterliste/Tabelle des zugehörigen Unternehmens durch eingegebene UID 
	 */
	private void onArbeiternehmerBtn() {
		mitarbeiterList.clear();
		mitarbeiterList = aktuellerZugriffMit.sucheArbeitnehmer(this.cbUID.getValue());
		grid.setItems(mitarbeiterList);
		grid.removeAllColumns();
		grid.addColumn(entry -> entry.getMitarbeiternummer()).setHeader("MitNr").setSortable(true);
		grid.addColumn(entry -> entry.getName()).setHeader("Name").setSortable(true);
		grid.addColumn(entry -> entry.getVorname()).setHeader("Vorname").setSortable(true);
		grid.addColumn(entry -> entry.getGehalt()).setHeader("Gehalt").setSortable(true);
		grid.addColumn(entry -> entry.getUID()).setHeader("UID").setSortable(true);
	}

	/**
	 * Erstellt Tabelle mit allen Mitarbeiter / Default Tabelle
	 */
	private void onAlleMitarbeiterBtn() {
		mitarbeiterList.clear();
		mitarbeiterList = aktuellerZugriffMit.mitarbeiterliste_db();
		grid.setItems(mitarbeiterList);
		grid.removeAllColumns();
		grid.addColumn(entry -> entry.getMitarbeiternummer()).setHeader("MitNr").setSortable(true);
		grid.addColumn(entry -> entry.getName()).setHeader("Name").setSortable(true);
		grid.addColumn(entry -> entry.getVorname()).setHeader("Vorname").setSortable(true);
		grid.addColumn(entry -> entry.getGehalt()).setHeader("Gehalt").setSortable(true);
		grid.addColumn(entry -> entry.getUID()).setHeader("UID").setSortable(true);
	}

	/**
	 * Methode für die nächst höchste MitarbeiterNr für Neuanlegungen
	 */
	private void nextNumber() {
		int maxNr = 0;
		leeren();
		mitarbeiterList = aktuellerZugriffMit.mitarbeiterliste_db();
		for (int i = 0; i < mitarbeiterList.size(); i++) {
			if (mitarbeiterList.get(i).getMitarbeiternummer() > maxNr) {
				maxNr = mitarbeiterList.get(i).getMitarbeiternummer();
			}
		}
		txtMaNr.setValue(Integer.toString(maxNr + 1));
		txtMaNr.setReadOnly(true);
		Notification.show("Mitarbeiterdaten eingeben, dann speichern");
	}
}
