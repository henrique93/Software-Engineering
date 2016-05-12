package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;

public class SuperUser extends SuperUser_Base {
    
    public SuperUser(MyDrive mydrive) {
        super();
        init("root", "rootroot", "SuperUser", "rwxdr-x-", mydrive);
    }

    public void SuperUser(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        initxml(mydrive, xml);
    }
    
    public void xmlImport(Element suserElement) throws ImportDocumentException, NotFileException {
        super.xmlImport(suserElement);
    }

    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("SuperUser");
        return element; 
    }
}
