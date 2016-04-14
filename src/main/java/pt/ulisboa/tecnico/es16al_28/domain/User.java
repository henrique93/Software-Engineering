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
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class User extends User_Base {
    
    /**
     *  Super User special constructor
     */
    public User(MyDrive mydrive) {
        setUsername("root");
        setPassword("rootroot");
        setName("Super User");
        setUmask("rwxdr-x-");
        setMydrive(mydrive);
        Login login = new Login("root", "rootroot");
        setDir(new Dir(login, "root", "rwxdr-x-", this));
	
    }
    
    /**
     *  User constructor
     *  @param  username    User's username
     *  @param  password    User's password
     *  @param  umask       User's mask
     *  @param  mydrive     MyDrive application
     */
    public User(String username, String password, String name, String umask, Login login) throws UserAlreadyExistsException , InvalidUsernameException, PermissionDeniedException {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(username);
        if (!(login.getUser().equals(login.getMydriveL().getUserByUsername("root")))) {
            throw new PermissionDeniedException();
        }
        if (m.find() || (username.length() < 3)) {
            throw new InvalidUsernameException(username);
        }
        for (User u: login.getMydriveL().getUserSet()) {
            if (u.getUsername() == username) {
                throw new UserAlreadyExistsException(username);
            }
        }
        Dir home = new Dir(login, name, umask, this);
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
        setDir(home);
        setMydrive(login.getMydriveL());
	
    }
    
    /**
     *  User XML constructor
     *  @param  mydrive     MyDrive application
     *  @param  xml	xml element
     */
    public User(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        setMydrive(mydrive);
        xmlImport(xml);
    }
    

    public void xmlImport(Element userElement) throws ImportDocumentException, NotFileException {
        for (File f: getFileSet())
            f.remove();

        try {
            setUsername(new String(userElement.getAttribute("username").getValue().getBytes("UTF-8")));
            setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
            setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setUmask(new String(userElement.getAttribute("umask").getValue().getBytes("UTF-8")));
            
        }   catch (UnsupportedEncodingException e) {
                throw new ImportDocumentException();
            }

        Element files = userElement.getChild("files");

		// FIXME this.getDir()
        for (Element dirElement: files.getChildren("dir"))
            new Dir(this.getMydrive(),this, this.getDir(), dirElement);

        for (Element plainfileElement: files.getChildren("plain-file"))
            new PlainFile(this.getMydrive(), this, this.getDir(), plainfileElement);

        for (Element appElement: files.getChildren("app"))
            new App(this.getMydrive(),this, this.getDir(), appElement);

        for (Element linkElement: files.getChildren("link"))
            new Link(this.getMydrive(),this, this.getDir(), linkElement);
        
    }
    
/
    public Element xmlExport() {
        Element element = new Element("user");
        element.setAttribute("username", getUsername());
        element.setAttribute("password", getPassword());
        element.setAttribute("name", getName());
        element.setAttribute("umask", getUmask());
        element.addContent(getDir().xmlExport());
        return element; 
    }
    
    /**
     *  User remover
     *  @param  mydrive     MyDrive application
     */
    public void remove() {
        Dir home = getDir();
        for (File f: home.getFile()) {
            f.remove();
        }
        setMydrive(null);
        deleteDomainObject();
    }
    
    /**
     *  User's description
     *  Example:
     *  Name: user     Username: username      Umask: rwxdr-x-
     */
    @Override
    public String toString() {
        return "Name:" + getName() + "Username:" + getUsername() + "Umask:" + getUmask();
    }
}
