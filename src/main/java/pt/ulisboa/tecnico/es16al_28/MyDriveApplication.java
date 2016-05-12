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
           /* for (String s: args) xmlScan(new File(s));
            print();*/
	    setup();
            //xmlPrint();

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
        /*User currentUser = app.getSuperUser();
        Dir currentDir = app.getRootDir();
        currentUser = new User("Alberto","lol", "Alberto", "rwxdr---", app);
        try {
            Login logged = new Login(app, "Alberto", "chumbartodos");
        } catch (IncorrectPasswordException e) {System.out.println(e);};
        currentDir = currentUser.getDir();
        PlainFile plainFile = new PlainFile(app, "FICHAALBERTO", "rwxdrw-d", currentUser, currentDir, "sasdsa");
        currentDir = new Dir(app, "Albertopasta", "rwxdr-x-", currentUser, currentDir);
        currentDir = app.getRootDir();
        Link link = new Link(app, "FICHAALBERTO", "rwxdrw-d", currentUser, currentDir, "asa");
        currentUser = new User("AlbertoMaria","chumbartodos", "Alberto Maria", "rwxdr---", app);
        currentDir = currentUser.getDir();
        plainFile = new PlainFile(app, "TEST", "rwxdrw-d", currentUser, currentDir,"assadsad");
        /*currentDir = currentDir.getParent();
        currentDir = new Dir(app, fileID++, "Turi", "rwxdr-x-", "Turi", currentDir);
        currentUser = new User("Turi", "1234", "tourette", "rwxdrw-d", currentDir, app);
        currentDir = currentDir.getParent();
        System.out.println(currentDir.listDir());
        PlainFile plainFile = new PlainFile(app, fileID++, "TEST", "rwxdrw-d", currentUser, currentDir);
        plainFile.setApp("FICHEIRO TESTE DE LEITURA DE CONTEUDO");
        System.out.println(plainFile.readFile());
        System.out.println(currentDir.listDir());
        currentDir.rm(currentUser.getUsername(), plainFile);
        System.out.println(currentDir.listDir());*/
    }
/*    
    @Atomic
    public static void print() {
        log.trace("Print: " + FenixFramework.getDomainRoot());
        PhoneBook pb = PhoneBook.getInstance();
        
        for (Person p: pb.getPersonSet()) {
            System.out.println("The Contact book of " + p.getName() + " contains " + p.getContactSet().size() + " contacts :");
            for (Contact c: p.getContactSet())
                System.out.println("\t" + c.getName() + " -> " + c.getPhoneNumber());
        }
    }
    */
    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = MyDrive.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }
   /*
    @Atomic
    public static void xmlScan(File file) {
        log.trace("xmlScan: " + FenixFramework.getDomainRoot());
        MyDrive app = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            app.xmlImport(document.getRootElement());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }*/
}
