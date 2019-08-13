package paketGUI;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.PWA;

import paketDatenbankzugriff.DBVerbindung;

import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a click listener.
 */
@Route
@PWA(name = "My Application", shortName = "My Application")
public class MainView extends VerticalLayout {
	
	Component activePage;
	private Tabs tabs;
	private Map<Tab, Component> tabsToPages;
	DBVerbindung verbindung;
	HorizontalLayout navBar;
	
    public MainView() {
    	this.setSizeFull();
    	this.getStyle().set("min-width", "0px");
    	this.getStyle().set("width", "");
    	this.getStyle().set("overflow", "auto");
    	this.getStyle().set("background", "linear-gradient(to right, #EAEAEA, #DBDBDB, #F2F2F2, #ADA996");

    	this.setPadding(false);
     	
		Tab mitarbeiterTab = new Tab("Mitarbeiter Tab");
		mitarbeiterTab.getStyle().set("color", "#0275d8");
		Tab unternehmenTab = new Tab("Unternehmen Tab");
		unternehmenTab.getStyle().set("color", "#0275d8");
		
		this.tabs = new Tabs(mitarbeiterTab, unternehmenTab);
		this.tabs.setFlexGrowForEnclosedTabs(1);
		
		this.tabsToPages = new HashMap<>();
		this.tabsToPages.put(mitarbeiterTab, new Fenster_Mitarbeiter());
		this.tabsToPages.put(unternehmenTab, new Fenster_Unternehmen());
		
		this.tabs.addSelectedChangeListener(e -> onTabChanged());
		this.activePage = this.tabsToPages.get(mitarbeiterTab);
		
		this.navBar = new HorizontalLayout();
    	navBar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    	navBar.setWidthFull();
    	navBar.setHeight("100px");
    	navBar.setSpacing(true);
    	navBar.setPadding(false);
    	navBar.getStyle().set("background", " linear-gradient(to top, #414345, #232526");  // W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7");
    	
    	Icon logo = new Icon(VaadinIcon.DATABASE);
    	logo.setSize("50px");
    	logo.getStyle().set("color", "white");
    	logo.getStyle().set("padding-left", "15px");
    	
    	VerticalLayout labelLayout = new VerticalLayout();
    	labelLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    	labelLayout.getStyle().set("border-style", "solid");
    	labelLayout.setPadding(false);
    	labelLayout.setSpacing(false);
    	labelLayout.setHeight("60%");
    	labelLayout.setWidth("125px");
    	Label mivLabel = new Label("MIV");
    	mivLabel.setHeight("25px");
    	mivLabel.getStyle().set("color", "white");
    	mivLabel.getStyle().set("font-weight", "bold");
    	mivLabel.getStyle().set("font-size", "20px");
    	mivLabel.getStyle().set("font-family", "Courier New");
    	Label mivLabelSmall = new Label("Mitarbeiter in Verwaltung");
    	mivLabelSmall.getStyle().set("align-self", "center");
    	mivLabelSmall.getStyle().set("color", "white");
    	mivLabelSmall.getStyle().set("font-size", "10px");
    	labelLayout.add(mivLabel, mivLabelSmall);
    	
      	navBar.add(logo, labelLayout,this.tabs);
    	this.add(navBar);
    	this.add(this.activePage);
    	
    }
    
	private void onTabChanged() {
		Tab selectedTab = this.tabs.getSelectedTab();
		this.remove(this.activePage);
		this.activePage = this.tabsToPages.get(selectedTab);
		this.add(this.activePage);
	}

}
