package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import org.jdom2.Document;

import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.PrintStream;


public class MyDrive extends MyDrive_Base {
	static final Logger log = LogManager.getRootLogger();

	public static MyDrive getInstance() {
        MyDrive app = FenixFramework.getDomainRoot().getMydrive();
        if (app != null) {
            return app;
        }
        else {
            log.trace("new Drive");
            return new MyDrive();
        }
    }

    public MyDrive() {
        setRoot(FenixFramework.getDomainRoot());
        setId(0);
        setRootDir(new Dir(this, new Dir(this)));
        setSuperUser(new User(this, getRootDir()));
    }
    
    public void cleanup() {
        for (User u: getUserSet()) {
            u.remove();
        }
    }
    
    public int incID() {
        int id = getId();
        id++;
        setId(id);
        return id;
    }

     public Document xmlExport() {
        Element element = new Element("MyDrive");
	Document doc = new Document(element);
        element.setAttribute("id", Integer.toString(getId()));
	for (User u: getUserSet())
        	element.addContent(u.xmlExport());
        return doc; 
    }

}
