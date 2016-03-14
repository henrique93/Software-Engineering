package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportUserException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.UserAlreadyExistsException;

import pt.ulisboa.tecnico.es16al_28.exception.UserAlreadyExistsException;

public class User extends User_Base {
    
    public User(String username, String password, String name, String umask, Dir home, MyDrive mydrive) throws UserAlreadyExistsException {
        for (User u: mydrive.getUserSet()) {
            if (u.getUsername() == username) {
                throw new UserAlreadyExistsException();
            }
        }
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
    public User(MyDrive mydrive, Dir parent, int dirID) {
        Dir dirRoot;
        setUsername("root");
        setPassword("rootroot");
        setName("Super User");
        setUmask("rwxdr-x-");
        setMydrive(mydrive);
        dirRoot = new Dir(dirID,"root","rwxdr-x-", parent);
        setDir(dirRoot);
	
    }


    
    @Override
    public String toString() {
        return "Name:" + getName() + "Username:" + getUsername() + "Umask:" + getUmask();
    }
    

	public void remove() {
        Dir home = getDir();
        for (File f: home.getFile()) {
            f.remove();
        }
        setMydrive(null);
        deleteDomainObject();
    }

     public void xmlImport(Element userElement) throws ImportDocumentException, NotFileException {

        try {
            setUsername(new String(userElement.getAttribute("username").getValue().getBytes("UTF-8")));
            setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
            setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setUmask(new String(userElement.getAttribute("umask").getValue().getBytes("UTF-8")));
            
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
     }

    public Element xmlExport() {
        Element element = new Element("user");
        element.setAttribute("username", getUsername());
        element.setAttribute("password", getPassword());
        element.setAttribute("name", getName());
        element.setAttribute("umask", getUmask());
        
        return element; 
    }
}
