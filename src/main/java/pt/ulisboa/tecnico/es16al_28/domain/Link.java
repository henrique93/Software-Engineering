package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.io.*;
import java.lang.reflect.*;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidContentException;


public class Link extends Link_Base {
    
    /**
     *  Link's constructor
     *  @param  login       Current MyDrive login
     *  @param  name        Link's name
     *  @param  app         Link's content
     */
    public Link(Login login, String name, String app) throws TooLongException, InvalidNameException, PermissionDeniedException, InvalidContentException {
        super();
        if (!validContent(app)) {
            throw new InvalidContentException(app);
        }
        User user = login.getUser();
        if (!inPathLimit(login, name)) {
            throw new TooLongException();
        }
        init(login.getMydriveL(), name, user.getUmask(), user, login.getCurrentDir());
        setApp(app);
    }

    /**
     *  XML Constructor
     */
    public Link(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
        initxml(mydrive, owner, dir,  xml);
    }

    /**
     *  Writes content in a Link
     *  @param  login       MyDrive's current login
     *  @param  app         Content to write
     */
    @Override
    public void writeFile(Login login, String app) throws PermissionDeniedException {
        File file = login.getFileByPath(getApp());
        file.writeFile(login, app);
    }

    /**
     *  Reads content from a Link
     *  @param  login       MyDrive's current login
     *  @return link's content
     */
    @Override
    public String readFile(Login login) throws PermissionDeniedException {
        File file = login.getFileByPath(getApp());
        return file.readFile(login);
    }
    
    /**
     *  Runs content from a Link
     *  @param  login       MyDrive's current login
     *  @param  args	    list of arguments
     */
    public void run(Login login, String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = login.getFileByPath(getApp());
        if (file.isLink()) {
            Link link = (Link) file;
            link.run(login, args);
        }
        if (file.isApp()) {
            App app = (App) file;
            String name = app.getApp();
            app.run(name, args);
        }
        if (file.isPlainFile()) {
            PlainFile plainFile = (PlainFile) file;
            plainFile.run(args);
        }

    }
    
    /**
     *  Checks if the content of the Link is valid
     *  @param  content     Link's content
     *  @return boolean     True if the content is valid for the Link, false otherwise
     */
    @Override
    public boolean validContent(String content) {
        if (content.contains("\0")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isPlainFile(){
        return false;
    }
    
    @Override
    public boolean isLink(){
        return true;
    }
    
    /**
     *  XML import
     */
    public void xmlImport(Element linkElement) throws ImportDocumentException {
		super.xmlImport(linkElement);
    }
    
    /**
     *  XML export
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("Link");
        return element; 
    }
    
}
