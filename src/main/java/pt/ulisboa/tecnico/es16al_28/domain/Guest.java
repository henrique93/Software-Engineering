package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;

public class Guest extends Guest_Base {
    
    public Guest(MyDrive mydrive) {
        super();
        init("nobody", null, "Guest", "rwxdr-x-", mydrive);
    }

     public void Guest(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        initxml(mydrive, xml);
    }

     public void xmlImport(Element guserElement) throws ImportDocumentException, NotFileException {
     		super.xmlImport(guserElement);
     }

     public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("Guest");
        return element; 
    }

    @Override
    public boolean checkPassword(String password) {
        return true;
    }

}
