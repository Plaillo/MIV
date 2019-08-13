package paketGUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataChangeEvent.DataRefreshEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import paketDatenbankzugriff.DBVerbindung;
import paketDatenbankzugriff.DBZugriff_Mitarbeiter;
import paketDatenbankzugriff.DBZugriff_Unternehmen;
import paketFachklassen.Mitarbeiter;
import paketFachklassen.Unternehmen;
public class Fenster_Unternehmen extends Div {

	Mitarbeiter aktuellerMitarbeiter;
	Unternehmen aktuellesUnternehmen;
	DBZugriff_Mitarbeiter aktuellerZugriffMit;
	DBZugriff_Unternehmen aktuellerZugriffUnt;
	DBVerbindung verbindung;
	Mitarbeiter meinMitarbeiter;
	Unternehmen meinUnternehmen;
	TextField tfSucheMitarbeiter = new TextField();
	TextField tfStadt = new TextField();
	TextField tfStrasse = new TextField();
	TextField tfHausNr = new TextField();
	TextField tfPlz = new TextField();
	Dialog loeschDialog;
	ComboBox<String> cbSucheMitarbeiter;
	private List<Unternehmen> unternehmenList;
	private int uID;
	private String uName;
	private ArrayList<String> mitarbeiterList;
	Grid<Unternehmen> grid;
	int value;
	
	private Grid<Unternehmen> createGrid(Collection<Unternehmen> unternehmenList) {
		this.grid = new Grid<>();
		
		grid.setHeightByRows(true);
		grid.setItems(unternehmenList);
		grid.getStyle().set("background-color", "gray");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setWidthFull();
		grid.getElement().setAttribute("theme", "compact");
		
		grid.addColumn(entry -> entry.getUnternehmensID()).setHeader("UID").setSortable(true);
		grid.addColumn(entry -> entry.getUntName()).setHeader("Name").setSortable(true);
		grid.addColumn(entry -> entry.getStadt()).setHeader("Stadt").setSortable(true);
		grid.addColumn(entry -> entry.getStrasse()).setHeader("Straße").setSortable(true);
		grid.addColumn(entry -> entry.getHausNr()).setHeader("HausNr").setSortable(true);
		grid.addColumn(entry -> entry.getPlz()).setHeader("PLZ").setSortable(true);
		grid.setVerticalScrollingEnabled(true);
		
		grid.addSelectionListener(event -> {
		    Set<Unternehmen> selected = event.getAllSelectedItems();
		    Notification.show("will be added later ;)");
		});
		return grid;
	}
	
	public Fenster_Unternehmen() {
		
		aktuellerZugriffMit = new DBZugriff_Mitarbeiter();
		aktuellerZugriffUnt = new DBZugriff_Unternehmen();
		aktuellerMitarbeiter = new Mitarbeiter();
		meinMitarbeiter = new Mitarbeiter();
		meinUnternehmen = new Unternehmen();
		mitarbeiterList =  new ArrayList<>();
		unternehmenList = aktuellerZugriffUnt.unternehmensliste_db();
		
		// Liste der UIDs für die ComboBox
		for (int i = 0; i < unternehmenList.size() ; i++) { 
		this.uID = unternehmenList.get(i).getUnternehmensID();
		this.uName = unternehmenList.get(i).getUntName();
		mitarbeiterList.add(Integer.toString(uID) + " " + uName);
		}
		if (unternehmenList == null) {
			leeren();
			Notification.show("keine passenden Mitarbeiter gefunden");
		} else {
		}
	
		this.setSizeFull();
		
		// ------------------------------ linkes Layout ------------------------------ //
		
		HorizontalLayout mainHorizontal = new HorizontalLayout();
		mainHorizontal.setWidthFull();
		
		VerticalLayout tfLayout = new VerticalLayout();
		tfLayout.setHeight("300px");
		tfLayout.setWidth("700px");
		tfLayout.setSpacing(true);
		tfLayout.setPadding(true);
		
		HorizontalLayout horizontal = new HorizontalLayout();
		horizontal.setHeight("100px");

		Label headline = new Label("Unternehmen verwalten");
		headline.getStyle().set("font-size", "24px");
		headline.getStyle().set("line-weight", "6px");
		tfLayout.add(headline);
		
		this.cbSucheMitarbeiter = new ComboBox<>("UID/Name d. Unternehmens");
		cbSucheMitarbeiter.setItems(mitarbeiterList);
		cbSucheMitarbeiter.setValue(mitarbeiterList.get(0));
		cbSucheMitarbeiter.addValueChangeListener(event -> {
			if (event.getSource().isEmpty()) {
				cbSucheMitarbeiter.setValue(mitarbeiterList.get(0));
			} else {
				suchenUnternehmenCB();
			}
		});
		horizontal.add(this.cbSucheMitarbeiter);

		this.tfStadt = new TextField("Stadt");
		this.tfStadt.setSizeFull();
		horizontal.add(this.tfStadt);
		
		this.tfStrasse = new TextField("Straße");
		this.tfStrasse.setSizeFull();
		horizontal.add(this.tfStrasse);
		
		HorizontalLayout horizontal2 = new HorizontalLayout();
		
		this.tfHausNr = new TextField("Hausnummer");
		this.tfHausNr.setSizeFull();
		horizontal2.add(this.tfHausNr);
		
		this.tfPlz = new TextField("PLZ");
		this.tfPlz.setSizeFull();
		horizontal2.add(this.tfPlz);
		
		// ------------------------------ rechtes Layout ------------------------------ //
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.setPadding(true);
		btnLayout.setSpacing(true);
		btnLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		btnLayout.setHeightFull();
		btnLayout.setWidth("350px");
		
		VerticalLayout buttonLayout = new VerticalLayout();

		Button leerenBtn = new Button("Felder leeren", VaadinIcon.TRASH.create());
		leerenBtn.addClickListener(e -> leeren());
		leerenBtn.setWidth("250px");

		Button anlegenBtn = new Button("Unternehmen anlegen", VaadinIcon.OFFICE.create());
		anlegenBtn.setWidth("250px");
		anlegenBtn.addClickListener(e -> anlegenUnternehmen());

		Button suchenBtn = new Button("Unternehmen suchen/anzeigen",  VaadinIcon.SEARCH.create());
		suchenBtn.addClickListener(e -> suchenUnternehmenCB());
		suchenBtn.setWidth("250px");
		suchenBtn.getStyle().set("font-size", "14px");

		Button aendernBtn = new Button("Unternehmen ändern", VaadinIcon.EDIT.create());
		aendernBtn.setWidth("250px");
		aendernBtn.addClickListener(e -> aendernUnternehmen());

		Button loeschenBtn = new Button("Unternehmen löschen", VaadinIcon.ERASER.create());
		loeschenBtn.setWidth("250px");
		loeschenBtn.addClickListener(e -> loeschenUnternehmen());
		
		Button refreshListBtn = new Button("refresh", VaadinIcon.REFRESH.create());
		refreshListBtn.setWidth("250px");
		refreshListBtn.addClickListener(e -> onRefreshListBtn());

//		Button aktualisierenBtn = new Button("Unternehmen aktualisieren");
//		aktualisierenBtn.setWidth("250px");
//		aktualisierenBtn.addClickListener(e -> onAktualisierenBtn());

		// ---------------------- Grid ---------------------- //
		this.grid = createGrid(unternehmenList);
		this.add(mainHorizontal);
		tfLayout.add(horizontal, horizontal2);
		mainHorizontal.add(tfLayout);
		buttonLayout.add(leerenBtn, anlegenBtn, aendernBtn, loeschenBtn, refreshListBtn);
		mainHorizontal.add(buttonLayout);
		this.add(grid);
	}
	
	public void suchenMitarbeiter() {
		aktuellerMitarbeiter = aktuellerZugriffMit.sucheMitarbeiter(cbSucheMitarbeiter.getValue().substring(0, 3));
		
		if (aktuellerMitarbeiter == null) {
			Notification.show("keinen Mitarbeiter gefunden");
		} else {
			anzeigenMitUntdaten();
		}
	}
	
	private void anzeigenMitUntdaten() {
//		tfSucheMitarbeiter.setValue(Integer.toString(aktuellerMitarbeiter.getMitarbeiternummer()));
		cbSucheMitarbeiter.setValue(aktuellerMitarbeiter.getName());
//		tfUnternehmen.setValue(aktuellesUnternehmen.getUntName());
	}
	
	/**
	 * flushed die aktuellen Daten
	 */
	private void leeren() {
		cbSucheMitarbeiter.clear();
		tfStadt.clear();
		tfStrasse.clear();
		tfHausNr.clear();
		tfPlz.clear();
	}
	
	/**
	 * Erfasst Inhalt aus dem TextField txtMaNr und gibt ihn an die Methode sucheUnternehmen() weiter.
	 * sucheUnternehmen erfasst die Werte aus der Datenbank und gibt sie wieder zurück.
	 * Die ausgelesenen Werte werden an die Methode anzeigenUnternehmendaten() weitergeleitet.
	 */
	public void suchenUnternehmenCB() {
		aktuellesUnternehmen = aktuellerZugriffUnt.sucheUnternehmen(cbSucheMitarbeiter.getValue().substring(0, 3));

		if (aktuellesUnternehmen == null) {
			leeren();
			Notification.show("kein Unternehmen gefunden");
		} else {
			anzeigenUnternehmendaten();
		}
	}
	/**
	 * Benutzt die ausgelesenen Werte der Methode sucheUnternehmen() und setzt sie in die entsprechenden Textfelder.
	 */
	private void anzeigenUnternehmendaten() {
		cbSucheMitarbeiter.setValue(aktuellesUnternehmen.getUnternehmensID() + " " + aktuellesUnternehmen.getUntName());
		tfStadt.setValue(aktuellesUnternehmen.getStadt());
		tfStrasse.setValue(aktuellesUnternehmen.getStrasse());
		tfHausNr.setValue(Integer.toString(aktuellesUnternehmen.getHausNr()));
		tfPlz.setValue(Integer.toString(aktuellesUnternehmen.getPlz()));
	}

	/**
	 * legt durch den Button anlegenBtn ("Unternehmen anlegen") einen neues Unternehmen an und fügt die Daten hinzu. 
	 * die Methode erfasseUnternehmen() oeffnet die Verbindung mit der Datenbank und fügt die Daten hinzu 
	 */
	private void anlegenUnternehmen() {
		Unternehmen neuesUnternehmen = new Unternehmen();
		try {
			neuesUnternehmen.setUntName(cbSucheMitarbeiter.getValue().substring(4, value));
			neuesUnternehmen.setStadt(tfStadt.getValue());
			neuesUnternehmen.setStrasse(tfStrasse.getValue()); // (txtVorname.getText());
			neuesUnternehmen.setHausNr(Integer.parseInt(tfHausNr.getValue()));
			neuesUnternehmen.setPlz(Integer.parseInt(tfPlz.getValue()));
			boolean ok = aktuellerZugriffUnt.erfasseUnternehmen(neuesUnternehmen);
			// Auswertung der Variablen ok
			if (ok == true) {
				Notification.show("Unternehmen erfasst");
			} else {
				Notification.show("Unternehmen nicht erfasst");
			}
		}
		// Abfangen von Eingabefehlern
		catch (Exception e) {
			Notification.show("Eingabefehler");
		}
	}
	
	/**
	 * ändert die Daten des Mitarbeiters in der Datenbank durch den Button aendernBtn ("Unternehmen ändern")
	 */
	private void aendernUnternehmen() {
		// Übertragung der geänderten Daten aus den Textfeldern in das Objekt
		this.value = cbSucheMitarbeiter.getValue().length();
		aktuellesUnternehmen.setUnternehmensID(Integer.parseInt(cbSucheMitarbeiter.getValue().substring(0, 3)));
		aktuellesUnternehmen.setUntName(cbSucheMitarbeiter.getValue().substring(4, value));
		aktuellesUnternehmen.setStadt(tfStadt.getValue());
		aktuellesUnternehmen.setStrasse(tfStrasse.getValue());
		aktuellesUnternehmen.setHausNr(Integer.parseInt(tfHausNr.getValue()));
		aktuellesUnternehmen.setPlz(Integer.parseInt(tfPlz.getValue()));
		
		// Methode aendereUnternehmen für das Objekt aktuellerZugriffUnt ausführen und das
		// Ergebnis in die Variable ok (boolean) übernehmen
		boolean ok = aktuellerZugriffUnt.aendereUnternehmen();
		// Rückgabewert der methode boolean aendereMitarbeiter() auswerten
		if (ok) {
			Notification.show("Datensatz geändert!");
		} else {
			Notification.show("Datensatz nicht geändert!");
		}
	}

	/**
	 * Führt ein Entscheidungsdialog aus, ob man den Mitarbeiter wirklich löschen will.
	 * "Ja" => Mitarbeiter wird gelöscht
	 * "Nein" => Mitarbeiter wird nicht gelöscht
	 * "Abbrechen" => Dialog wird geschlossen
	 */
	private void loeschenUnternehmen() {
		// Eingabedialogfenster
		this.loeschDialog = new Dialog();
		loeschDialog.setWidth("400px");
		loeschDialog.setHeight("50px");
		Label wirklichLoeschenLbl = new Label("Wirklich Löschen?");
		loeschDialog.add(wirklichLoeschenLbl);
		Button yesBtn = new Button("Ja", event -> unternehmenLoeschenAbfrage());
		Button noBtn = new Button("Nein", event -> unternehmenNichtLoeschenAbfrage());
		Button cancelnBtn = new Button("Abbrechen", event -> loeschDialog.close());
		loeschDialog.add(yesBtn, noBtn, cancelnBtn);
		loeschDialog.open();
	}

	/**
	 * loeschenMitarbeiter() Option: "Ja"
	 */
	public void unternehmenLoeschenAbfrage() {
		this.value = cbSucheMitarbeiter.getValue().length();
		meinUnternehmen.setUntName(cbSucheMitarbeiter.getValue().substring(4, value));
		boolean ok = aktuellerZugriffUnt.loescheUnternehmen();
		if (ok) {
			Notification.show("Unternehmen gelöscht!!");
			this.loeschDialog.close();
		} else {
			Notification.show("Unternehmen nicht gelöscht!");
			this.loeschDialog.close();
		}
		leeren();
	}
	/**
	 * loeschenMitarbeiter() Option: "Nein"
	 */
	public void unternehmenNichtLoeschenAbfrage() {
		Notification.show("Unternehmen nicht gelöscht!");
		this.loeschDialog.close();
	}
	
	public void onRefreshListBtn() {
		unternehmenList.clear();
		unternehmenList = aktuellerZugriffUnt.unternehmensliste_db();
		grid.setItems(unternehmenList);
		grid.removeAllColumns();
		grid.addColumn(entry -> entry.getUnternehmensID()).setHeader("UID").setSortable(true);
		grid.addColumn(entry -> entry.getUntName()).setHeader("Name").setSortable(true);
		grid.addColumn(entry -> entry.getStadt()).setHeader("Stadt").setSortable(true);
		grid.addColumn(entry -> entry.getStrasse()).setHeader("Straße").setSortable(true);
		grid.addColumn(entry -> entry.getHausNr()).setHeader("HausNr").setSortable(true);
		grid.addColumn(entry -> entry.getPlz()).setHeader("PLZ").setSortable(true);
	}
}
