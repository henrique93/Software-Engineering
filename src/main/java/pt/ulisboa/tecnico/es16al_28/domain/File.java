package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.io.UnsupportedEncodingException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;


import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void init(MyDrive mydrive, String name, String permission, User owner, Dir dir) {
        setId(mydrive.incID());
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission(permission);
        setOwner(owner);
        setParent(dir);
    }
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
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
     *
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
    public void remove(User user) throws PermissionDeniedException{
        if (userHasPermissionRemove(user)){
      		setParent(null);
       	 	setOwner(null);
        	deleteDomainObject();
        }
    }

    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
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
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
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
	
    public boolean isDir(){
        return false;
    }

    public boolean isFile(){
        return true;
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
