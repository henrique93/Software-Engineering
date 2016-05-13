package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.io.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidContentException;
import pt.ulisboa.tecnico.es16al_28.exception.CyclicLinkException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;


public class Link extends Link_Base {

    private List<Link> _linkList = new ArrayList<Link>();

    /**
     *  Link's constructor
     *  @param  login       Current MyDrive login
     *  @param  name        Link's name
     *  @param  app         Link's content
     */
    public Link(Login login, String name, String app) throws TooLongException, InvalidNameException, PermissionDeniedException, InvalidContentException, CyclicLinkException {
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


    public boolean isNotCyclic(Login login, String app) {
        File file = login.getFileByPath(app);
        while (file.isLink()) {
            Link link = (Link) file;
            if (_linkList.contains(link)) {
                _linkList.clear();
                return false;
            }
            _linkList.add(link);
            file = login.getFileByPath(link.getApp());
        }
        _linkList.clear();
        return true;
    }


    /**
     *  Writes content in a Link
     *  @param  login       MyDrive's current login
     *  @param  app         Content to write
     */
    @Override
    public void writeFile(Login login, String app) throws PermissionDeniedException {
        if (isNotCyclic(login, getApp())) {
            File file = login.getFileByPath(getApp());
            file.writeFile(login, app);
        }
        else {
            throw new CyclicLinkException();
        }
    }

    /**
     *  Reads content from a Link
     *  @param  login       MyDrive's current login
     *  @return link's content
     */
    @Override
    public String readFile(Login login) throws PermissionDeniedException {
        if (isNotCyclic(login, getApp())) {
            File file = login.getFileByPath(getApp());
            return file.readFile(login);
        }
        else {
            throw new CyclicLinkException();
        }
    }


    /**
     *  Reads content from a Link
     *  @param  login       MyDrive's current login
     *  @return link's content
     */
    public void run(Login login, String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, CyclicLinkException{
        if (isNotCyclic(login, getApp())) {
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
        else {
            throw new CyclicLinkException();
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
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public Link(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
     	initxml(mydrive, owner, dir,  xml); 	
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public void xmlImport(Element linkElement) throws ImportDocumentException {
		super.xmlImport(linkElement);
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("Link");
        return element; 
    }
    
}

