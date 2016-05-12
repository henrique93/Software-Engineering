package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;

public class Guest extends Guest_Base {

    /**
     *  Guest constructor
     *  Username: nobody
     *  Password: null
     *  Name: Guest
     *  mask: r-x-
     */
    public Guest(MyDrive mydrive) {
        super();
        init("nobody", null, "Guest", "rwxdr-x-", mydrive);
    }

    /**
     *  XML Constructor
     */
    public void Guest(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        initxml(mydrive, xml);
    }

    /**
     *  XML Import
     */
    public void xmlImport(Element guserElement) throws ImportDocumentException, NotFileException {
        super.xmlImport(guserElement);
    }

    /**
     *  XML Export
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("Guest");
        return element; 
    }

    /**
     *  Guest does not have a password so the password checker always returns true
     */
    @Override
    public boolean checkPassword(String password) {
        return true;
    }

}
