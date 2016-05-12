package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.io.*;
import java.util.*;

/* Import exceptions */
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;

public class PlainFile extends PlainFile_Base {
    
    //run operation related 	
    private static PrintWriter out = new PrintWriter(System.out, true);
    public static void output(PrintWriter pw) { out = pw; }
    
    public PlainFile () {
        super();
    }
    
    /**
     *  PlainFile constructor
     *  @param  login       Current MyDrive login
     *  @param  name        PlainFile's name
     *  @param  app         PlainFile's content
     */
    public PlainFile(Login login, String name, String app) throws TooLongException, InvalidNameException, PermissionDeniedException {
        super();
        User user = login.getUser();
        if (!inPathLimit(login, name)) {
            throw new TooLongException();
        }
        init(login.getMydriveL(), name, user.getUmask(), user, login.getCurrentDir());
        setApp(app);
    }
    
    /**
     *  XML initializer
     */
    public PlainFile(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
    	initxml(mydrive, owner, dir,  xml);
    }
    
    /**
     *  Reads the content from a file
     *  @param  l           MyDrive's current login
     *  @return string      File's content
     */
    @Override
    public String readFile(Login l) throws PermissionDeniedException{
        User user = l.getUser();
        String username = user.getUsername();
        String umask = user.getUmask();
        if (hasPermission(l, "r")) {
            return getApp();
        }
        else {
            throw new PermissionDeniedException();
        }
    }
     
    /**
     *  Writes content in a PlainFile
     *  @param  login       MyDrive's current login
     *  @param  app         Content to write
     */
    @Override
    public void writeFile(Login l,String app) throws PermissionDeniedException {
        User user = l.getUser();
        String username = user.getUsername();
        String umask = user.getUmask();
        if (hasPermission(l, "w")) {
            setApp(app);
        }
        else {
            throw new PermissionDeniedException();
        }
    }

    /**
     *  Checks if the content of the PlainFile is valid
     *  @param  content     PlainFile's content
     *  @return boolean     True if the content is valid for the PlainFile, false otherwise
     */
    public boolean validContent(String content) {
        return true;
    }
    
    /**
     *  XML import
     */
    public void xmlImport(Element plainfileElement) throws ImportDocumentException {
        
    	super.xmlImport(plainfileElement);

    	try {
            setApp(new String(plainfileElement.getAttribute("app").getValue().getBytes("UTF-8")));

	} catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
    }
    
    /**
     *  XML export
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setAttribute("app", getApp());
	element.setName("PlainFile");
        return element; 
    }

    /**
     *  Checks if it is a PlainFile
     */
    @Override
    public boolean isPlainFile(){
        return true;
    }

    /**
     *  Runs content from a PlainFile
     *  @param  args	    list of arguments
     */
    public static void run(String[] args) throws IOException {
        String input;
        Thread master = Thread.currentThread();
        Scanner scan = new Scanner(System.in);

        ProcessBuilder builder;
        if (args.length == 0) builder = new ProcessBuilder("/bin/bash");
        else {
            java.util.List<String> l = new ArrayList<String>();
            for (String s: args) l.add(s);
            builder = new ProcessBuilder(l);
        }
        builder.redirectErrorStream(true);
        Process proc = builder.start();
        OutputStream stdin = proc.getOutputStream ();
        InputStream stdout = proc.getInputStream ();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        Thread throut = new Thread(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    while ((line = reader.readLine ()) != null) {
                        out.println ("Stdout: " + line);
                    }
                } catch (IOException e) { e.printStackTrace(); }
                System.err.println ("Stdout is now closed!!!");
            }
        } );
        throut.start();
        
        for (;;) {
            do
                try { Thread.sleep(100);
                } catch (InterruptedException e) { }
            while(proc.isAlive() && !scan.hasNext());
            if (proc.isAlive()) {
                if ((input = scan.nextLine()) != null) {
                    writer.write(input);
                    writer.newLine();
                    writer.flush();
                }
            } else break;
        }
        
        try { proc.waitFor();
        } catch (InterruptedException e) { }

        System.err.println ("exit: " + proc.exitValue());
        proc.destroy();
    }
}
