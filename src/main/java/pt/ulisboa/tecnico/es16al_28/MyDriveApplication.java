package pt.ulisboa.tecnico.es16al_28;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;


import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* Imports FenixFramework */
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import pt.ulisboa.tecnico.es16al_28.exception.IncorrectPasswordException;

/* Import domain */
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Login;

public class MyDriveApplication {
    static final Logger log = LogManager.getRootLogger();
    
    public static void main(String[] args) throws IOException {
        System.out.println("*** Welcome to the MyDrive application! ***");
        try {
	    setup();
            xmlPrint();

        } finally {  System.out.println("*** Work done, shutting down ***");
	FenixFramework.shutdown(); }
    }
    
    @Atomic
    public static void init() { // empty MyDrive
        log.trace("Init: " + FenixFramework.getDomainRoot());
    }

    @Atomic
    public static void setup() { // MyDrive with debug data
        log.trace("Setup: " + FenixFramework.getDomainRoot());
        MyDrive app = MyDrive.getInstance();

    }

    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = MyDrive.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }
}
