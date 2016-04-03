package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportUserException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.UserAlreadyExistsException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidUsernameException;

public class User extends User_Base {
    
    public User(String username, String password, String name, String umask, MyDrive mydrive) throws UserAlreadyExistsException , InvalidUsernameException {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(username);
        if (m.find()) {
            throw new InvalidUsernameException(username);
        }
        for (User u: mydrive.getUserSet()) {
            if (u.getUsername() == username) {
                throw new UserAlreadyExistsException(username);
            }
        }
        Dir home = new Dir(mydrive, name, umask, this, mydrive.getRootDir());
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
        setDir(home);
        setMydrive(mydrive);
	
    }
    
    public User(Dir home, MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        setDir(home);
        setMydrive(mydrive);
        xmlImport(xml);


    }

    /**
     *  Super User special constructor
     */
    public User(MyDrive mydrive, Dir parent) {
        Dir dirRoot;
        setUsername("root");
        setPassword("rootroot");
        setName("Super User");
        setUmask("rwxdr-x-");
        setMydrive(mydrive);
        dirRoot = new Dir(mydrive, "root", "rwxdr-x-", this, parent);
        setDir(dirRoot);
	
    }
    
    @Override
    public String toString() {
        return "Name:" + getName() + "Username:" + getUsername() + "Umask:" + getUmask();
    }
    
    public void xmlImport(Element userElement) throws ImportDocumentException, NotFileException {
        try {
            setUsername(new String(userElement.getAttribute("username").getValue().getBytes("UTF-8")));
            setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
            setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setUmask(new String(userElement.getAttribute("umask").getValue().getBytes("UTF-8")));
            
        }   catch (UnsupportedEncodingException e) {
                throw new ImportDocumentException();
            }
        
    }

    public Element xmlExport() {
        Element element = new Element("user");
        element.setAttribute("username", getUsername());
        element.setAttribute("password", getPassword());
        element.setAttribute("name", getName());
        element.setAttribute("umask", getUmask());
        element.addContent(getDir().xmlExport());
        return element; 
    }

	public void remove() {
        Dir home = getDir();
        for (File f: home.getFile()) {
            f.remove();
        }
        setMydrive(null);
        deleteDomainObject();
    }
}
