package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.UnsupportedEncodingException;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;

public abstract class File extends File_Base {

    public File() {
        super();
    }
    
    /**
     *  File initializer
     *  @param  mydrive     MyDrive application
     *  @param  name        File's name
     *  @param  permission  File's permissions
     *  @param  owner       User who owns this file (is either it's creator or the Super User)
     *  @param  dir         Directory on which this file is located
     */
    public void init(MyDrive mydrive, String name, String permission, User owner, Dir dir) throws InvalidNameException, PermissionDeniedException {
        if (name.contains("/") || name.contains("\0")) {
            throw new InvalidNameException(name);
        }
        String username = owner.getUsername();
        if (!username.equals("root")) {
            User parentOwner = dir.getOwner();
            String pOwner = parentOwner.getUsername();
            if (!username.equals(pOwner)) {
                throw new PermissionDeniedException();
            }
        }
        setId(mydrive.incID());
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission(permission);
        setOwner(owner);
        setParent(dir);
    }

    /**
     *  Special File initializer for "/" and "home" directory
     *  @param  mydrive     MyDrive application
     *  @param  dir         Directory on which this file is located
     */
    public void initRoot(MyDrive mydrive, String name, Dir dir) {
        setId(mydrive.incID());
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission("rwxdr-x-");
        setOwner(mydrive.getSuperUser());
        setParent(dir);
    }

    /**
     *  Special File initializer for user's "home" directory
     *  @param  mydrive     MyDrive application
     *  @param  dir         Directory on which this file is located
     */
    public void initHome(MyDrive mydrive, String name, String permission, User owner) {
        setId(mydrive.incID());
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission(permission);
        setOwner(owner);
        setParent(mydrive.getRootDir());
    }


    /**
     *  XML initializer
     */
    public void initxml(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
    	setId(mydrive.incID());
    	setOwner(owner);
        setParent(dir);
        xmlImport(xml);
    }


    /**
     *  If user doesnt have permission, it throws an exception
     *  @param  user        Current user
     *  @param  file        File to be removed
     *  @return true	    Returns true if user has permission, else throws exception
     */

    public boolean userHasPermissionRemove(User user) throws PermissionDeniedException{
        String umask = user.getUmask();
        String username = user.getUsername();
        String ownerUsername = getOwner().getUsername();
        if (umask.charAt(7) != 'd' || getPermission().charAt(7) != 'd') {
            if (!username.equals("root") && !username.equals(ownerUsername)) {
                throw new PermissionDeniedException();
            }
        }
        return true;
    }
  
    /**
     *  File remover
     */
    public void remove(User user) throws PermissionDeniedException {
        if (userHasPermissionRemove(user)){
      		setParent(null);
       	 	setOwner(null);
        	deleteDomainObject();
        }
    }


    /**
     *  Writes content in a File
     *  @param  login       MyDrive's current login
     *  @param  app         Content to write
     */
    public void writeFile(Login login, String content) throws NotFileException {
        throw new NotFileException(getName());
    }


    /**
     *  Reads a file's content
     *  @param  login       MyDrive's current login
     *  @return file's content
     */
    public String readFile(Login login) throws NotFileException {
        throw new NotFileException(getName());
    }

    /**
     *  XML import
     */
    public void xmlImport(Element fileElement) throws ImportDocumentException {
        try {
            setName(new String(fileElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setPermission(new String(fileElement.getAttribute("permission").getValue().getBytes("UTF-8")));

        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
    }
    
    /**
     *  XML export
     */
    public Element xmlExport() {
        Element element = new Element("File");
    	element.setAttribute("id", Integer.toString(getId()));
        element.setAttribute("name", getName());
        element.setAttribute("lastChange",  getLastChange());
        element.setAttribute("permission", getPermission());
        return element;
    }

    /**
     *  Checks if the new file's path will exceed the limit
     *  @param  login       Current login
     *  @param  name        File's name
     *  @return boolean     True if path does not exceed the limit, false otherwise
     */
    public boolean inPathLimit(Login login, String name) {
        Dir cwd = login.getCurrentDir();
        String path = cwd.absolutePath();
        if((path.length() + 1 + name.length()) > 1024) {
            return false;
        }
        return true;
    }



    /**
     *  Checks if the current user has a certains permission to this file
     *  @param  login       MyDrive's current login
     *  @param  permission  Type of permission to check (read, write, execute or delete)
     *  @return boolean     True if the current user has the given permisison to the file, false otherwise
     */
    public boolean hasPermission(Login login, String permission) throws PermissionDeniedException {
        int permissionCharNr = 0;
        char permissionChar;;
        switch (permission.toLowerCase()) {
            case "read":		permissionCharNr = 4;
                                permissionChar = 'r';
                                break;
            case "r":           permissionCharNr = 4;
                                permissionChar = 'r';
                                break;

            case "write":		permissionCharNr = 5;
                                permissionChar = 'w';
                                break;
            case "w":           permissionCharNr = 5;
                                permissionChar = 'w';
                                break;

            case "execute":     permissionCharNr = 6;
                                permissionChar = 'x';
                                break;
            case "x":           permissionCharNr = 6;
                                permissionChar = 'x';
                                break;

            case "delete":		permissionCharNr = 7;
                                permissionChar = 'd';
                                break;
            case "d":           permissionCharNr = 7;
                                permissionChar = 'd';
                                break;

            default:            return false;
                
        }
        User user = login.getUser();
        String mask = user.getUmask();
        String username = user.getUsername();
        boolean userHasPermission = (mask.charAt(permissionCharNr) == permissionChar);
        boolean fileHasPermission = (getPermission().charAt(permissionCharNr) == permissionChar);
        if ((userHasPermission && fileHasPermission) || username.equals("root")) {
            return true;
        }
        else {
            return false;
        }
    }


    public void changeOwner(User owner) {
        super.setOwner(owner);
    }

    public void changePermission(String permission) {
        super.setPermission(permission);
    }
	
    public boolean isDir(){
        return false;
    }

    public boolean isFile(){
        return true;
    }

	public boolean isApp(){
		return false;
    }

    public boolean isLink(){
        return false;
    }

    public boolean isPlainFile(){
        return false;
    }


    /**
     *  File's description
     *  Example:
     *  "File ID: 21   Name: File  Owner: User     Permissions: rwxdr---   Modified last at: 2016.03.20.15.36.28"
     */
    @Override
    public String toString() {
        return "File ID: " + getId() + "\tName: " + getName() + "\tOwner: " + getOwner().getUsername() + "\tPermisisons: " + getPermission() + "\tModified last at: " + getLastChange();
    }
}
