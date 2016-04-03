package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

public class App extends App_Base {
    
    public App(MyDrive mydrive, String name, String permission, User owner, Dir dir, String app) {
        super();
        init(mydrive, name, permission, owner, dir);
	super.setApp(app);
    }
    
    public Element xmlExport() {
        Element element = super.xmlExport();
	element.setName("App");
        return element; 
    }
}
