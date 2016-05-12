package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;

public class SuperUser extends SuperUser_Base {

    /**
     *  SuperUser constructor
     *  Username: root
     *  Password: rootroot
     *  Name: SuperUser
     *  mask: r-x-
     */
    public SuperUser(MyDrive mydrive) {
        super();
        init("root", "rootroot", "SuperUser", "rwxdr-x-", mydrive);
    }

    /**
     *  XML Constructor
     */
    public void SuperUser(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        initxml(mydrive, xml);
    }

    /**
     *  XML Import
     */
    public void xmlImport(Element suserElement) throws ImportDocumentException, NotFileException {
        super.xmlImport(suserElement);
    }

    /**
     *  XML Export
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("SuperUser");
        return element; 
    }
}
