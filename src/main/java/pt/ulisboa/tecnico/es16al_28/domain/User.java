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
import pt.ulisboa.tecnico.es16al_28.exception.InvalidPasswordException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class User extends User_Base {

    public User() {
        super();
    }

    /**
     *  User constructor
     *  @param  username    User's username
     *  @param  password    User's password
     *  @param  umask       User's mask
     *  @param  mydrive     MyDrive application
     */
    public User(String username, String password, String name, String umask, Login login) throws UserAlreadyExistsException , InvalidUsernameException, InvalidPasswordException, PermissionDeniedException {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(username);
        MyDrive mydrive = login.getMydriveL();
        if (!(login.getUser().equals(mydrive.getUserByUsername("root")))) {
            throw new PermissionDeniedException();
        }
        if (m.find() || (username.length() < 3)) {
            throw new InvalidUsernameException(username);
        }
        if (password.length() < 8) {
            throw new InvalidPasswordException();
        }
        for (User u: mydrive.getUserSet()) {
            String existentUsername = u.getUsername();
            if (existentUsername.equals(username)) {
                throw new UserAlreadyExistsException(username);
            }
        }
        super.setUsername(username);
        Dir home = new Dir(login, name, umask, this);
        super.setPassword(password);
        super.setName(name);
        super.setUmask(umask);
        super.setDir(home);
        super.setMydrive(mydrive);
	
    }

    /**
     *  User initializer
     *  @param  username    User's username
     *  @param  password    User's password
     *  @param  name        User's name
     *  @param  umask       User's mask
     *  @param  mydrive     MyDrive application
     */
    public void init(String username, String password, String name, String umask, MyDrive mydrive) {
        super.setUsername(username);
        super.setPassword(password);
        super.setName(name);
        super.setUmask(umask);
        super.setMydrive(mydrive);
        Login login = new Login(username, password);
        super.setDir(new Dir(login, username, umask, this));
    }

    /**
     *  User XML constructor
     *  @param  mydrive     MyDrive application
     *  @param  xml	xml element
     */
    public User(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        initxml(mydrive,xml);
    }

    /**
     *  XML Initializer
     */
    public void initxml(MyDrive mydrive, Element xml) throws ImportDocumentException, NotFileException {
        setMydrive(mydrive);
        xmlImport(xml);
    }
    
    /**
     *  XML Import
     */
    public void xmlImport(Element userElement) throws ImportDocumentException, NotFileException {
        for (File f: getFileSet())
            f.remove(this);

        try {
            super.setUsername(new String(userElement.getAttribute("username").getValue().getBytes("UTF-8")));
            super.setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
            super.setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
            super.setUmask(new String(userElement.getAttribute("umask").getValue().getBytes("UTF-8")));
            
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
    

    /**
     *  XML Export
     */
    public Element xmlExport() {
        Element element = new Element("user");
        element.setAttribute("username", getUsername());
        element.setAttribute("password", super.getPassword());
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
        for (File file: home.getFile()) {
            file.remove(this);
        }
        super.setMydrive(null);
        deleteDomainObject();
    }

    /**
     *  Check if the given password is the same as this user's password
     *  @param  password
     *  @return boolean     true if the password is correct, false otherwise
     */
    public boolean checkPassword(String password) {
        if (password.equals(super.getPassword())) {
            return true;
        }
        else {
            return false;
        }
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


    /* OVERRIDE GETTERS AND SETTERS FOR SECURITY */
    @Override
    public String getPassword() throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setPassword(String password) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setUsername(String username) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setName(String name) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setMydrive(MyDrive mydrive) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setDir(Dir dir) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setUmask(String umask) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }
}
