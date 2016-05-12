package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.lang.reflect.*;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidContentException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class App extends App_Base {
    
    /**
     *  Common App constructor
     *  @param  login       Current MyDrive login
     *  @param  name        App's name
     *  @param  app         App's content
     */
    public App(Login login, String name, String app) throws TooLongException, InvalidNameException, PermissionDeniedException {
        super();
        if (!inPathLimit(login, name)) {
            throw new TooLongException();
        }
        if (!validContent(app)) {
            throw new InvalidContentException(app);
        }
        User user = login.getUser();
        init(login.getMydriveL(), name, user.getUmask(), user, login.getCurrentDir());
        setApp(app);
    }
    
      /**
     *  App's constructor for XML
     *  @param  login       Current MyDrive login
     *  @param  owner        App's owner
     *  @param  dir         App's dir
     *  @param  xml	xml element
     */
    public App(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {

     	initxml(mydrive, owner, dir,  xml);	
    }
    


    public void xmlImport(Element appElement) throws ImportDocumentException {

		super.xmlImport(appElement);

    }
    
   
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("App");
        return element; 
    }


    /**
     *  Writes content in a App
     *  @param  login       MyDrive's current login
     *  @param  app         Content to write
     */
    @Override
    public void writeFile(Login login, String app) throws PermissionDeniedException, InvalidContentException {
        if (!validContent(app)) {
            throw new InvalidContentException(app);
        }
        super.writeFile(login, app);
    }


    /**
     *  Checks if the content of the App is valid
     *  @param  content     App's content
     *  @return boolean     True if the content is valid for the App, false otherwise
     */
    @Override
    public boolean validContent(String content) {
        Pattern p = Pattern.compile("[^A-Za-z0-9._]");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return false;
        }
        return true;
    }


	@Override
	public boolean isApp(){
		return true;
    }

    @Override
    public boolean isPlainFile(){
        return false;
    }

     /**
     *  Call a static method with argument String[]
     *  (Return value is ignored)
     *  @param name full-class-name or full-method-name
     *  @param args arguments to the function
     */
    public static void run(String name, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class<?> cls;
        Method meth;
        try { // name is a class: call main()
            cls = Class.forName(name);
            meth = cls.getMethod("main", String[].class);
        } catch (ClassNotFoundException cnfe) { // name is a method
            int pos;
            if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
            cls = Class.forName(name.substring(0, pos));
            meth = cls.getMethod(name.substring(pos+1), String[].class);
        }
        meth.invoke(null, (Object)args); // static method (ignore return)
    }
}