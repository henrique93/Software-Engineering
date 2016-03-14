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

/* Import domain */
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.User;

public class MyDriveApplication {
    static final Logger log = LogManager.getRootLogger();
    
    public static void main(String[] args) throws IOException {
        System.out.println("*** Welcome to the MyDrive application! ***");
        try {
            init();
            setup();

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
        int fileID = 1;
        User currentUser;
        Dir currentDir = new Dir();
        currentDir = new Dir(fileID++, "home", "rwxdr-x-", currentDir);
        User superuser = new User(MyDrive.getInstance(), currentDir, fileID++);

    }
}
