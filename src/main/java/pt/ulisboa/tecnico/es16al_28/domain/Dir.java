package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.util.*;



/* Import exceptions */
import java.io.UnsupportedEncodingException;
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.FileNotFoundException;
import pt.ulisboa.tecnico.es16al_28.exception.FileAlreadyExistsException;
import pt.ulisboa.tecnico.es16al_28.exception.NotEmptyException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;

public class Dir extends Dir_Base {
    
    /**
     *  Special constructor for directory "/"
     *  @param  mydrive     MyDrive application
     */
    public Dir(MyDrive mydrive) {
        super();
        init(mydrive, "/", "rwxdr-x-", mydrive.getSuperUser(), this);
    }

    /**
     *  Special constructor for directory "home"
     *  @param  mydrive     MyDrive application
     *  @param  dir         directory "/"
     */
    public Dir(MyDrive mydrive, Dir dir) {
        super();
        init(mydrive, "home", "rwxdr-x-", mydrive.getSuperUser(), dir);
    }

    /**
     *  Special constructor for user's home directory
     *  @param  login       Current MyDrive login
     *  @param  name        Directory's name
     */
    public Dir(Login login, String name, String permission, User user) throws FileAlreadyExistsException {
        super();
        for (File file: getFileSet()) {
            if (file.getName() == name) {
                throw new FileAlreadyExistsException(name);
            }
        }
        init(login.getMydriveL(), name, permission, user, login.getMydriveL().getRootDir());
    }

    /**
     *  Common directory constructor
     *  @param  login       Current MyDrive login
     *  @param  name        Directory's name
     */
    public Dir(Login login, String name) throws FileAlreadyExistsException {
        super();
        for (File file: getFileSet()) {
            if (file.getName() == name) {
                throw new FileAlreadyExistsException(name);
            }
        }
        init(login.getMydriveL(), name, login.getUser().getUmask(), login.getUser(), login.getCurrentDir());
    }
    
    
    public Dir(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
    	initxml(mydrive, owner, dir,  xml);
    }
    
    /**
     *  Directory's simple listing. Lists every file in this directory
     *  @return ls          List of every file in this directory
     */
    public String listDir() {
        String ls = ".\n..\n"; /* . e .. ou nomes??? */
        for (File file: getFileSet()) {
            ls += file.toString() + "\n";
        }
        return ls;
    }
    

    /**
     *  Directory's simple listing. Lists every file in this directory
     *  @return ls          List of every file in this directory
     */
    public List<String> listD() {
        List<String> ls = new ArrayList<>();
        ls.add(toString());
        ls.add(getParent().toString());
        for (File file: getFileSet()){
            ls.add(file.toString());
        }
        return ls;
    }
    
    
    /**
     *  Remove a file or an empty directory from the current directory if the user has permission.
     *  @param  username    Current user's username
     *  @param  file        File to be removed
     */
    public void rm(String username, File file) throws PermissionDeniedException, FileNotFoundException, NotEmptyException {
        User owner = file.getOwner();
        if ((owner.getUsername() != "root") && (owner.getUsername() != username)) {
            throw new PermissionDeniedException();
        }
        Set entries = getFileSet();
        if (entries.contains(file)) {
            if (file instanceof Dir) {
                Dir dir = (Dir) file;
                if (dir.getParent().equals(dir)) {
                    throw new PermissionDeniedException();
                }
                else if (dir.getFileCount() > 1) {
                    throw new NotEmptyException(dir.getName());
                }
            }
            removeFile(file);
            remove();
        }
        else {
            throw new FileNotFoundException(file.getName());
        }
    }


   /**
     *  Get File inside the current directory by name
     *  @param  name        Name of file
     *  @return f           File with the name name
     */

    public File getFileByName(String name) throws NoSuchFileOrDirectoryException {
        for (File f: getFileSet()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        throw new NoSuchFileOrDirectoryException(name);
    }

    /**
     *  Checks if the current directory has the file
     *  @param  name        Name of file
     *  @return f           File with the name name
     */

    public boolean directoryHasFile(String name) {
        for (File f: getFileSet()) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  If user doesnt have permission, it throws an exception
     *  @param  user        Current user
     *  @param  file        File to be removed
     *
     *  @return true	    Returns true if user has permission, else throws exception
     */

    public boolean userHasPermissionRemove(User user, File file) throws PermissionDeniedException{
	
	if(user.getUmask().charAt(7) != 'd' || file.getPermission().charAt(7) != 'd')
		if(user.getUsername() != "root" && user.getUsername() != getOwner().getUsername())
			throw new PermissionDeniedException();
	return true;

    }

    /**
     *	File remover : remove especific file inside current Dir except if it finds a Dir, then it proceeds differently
     */
    public void removeFile(User user, String name) throws PermissionDeniedException, NoSuchFileOrDirectoryException{
        if(directoryHasFile(name)){
		File f = getFileByName(name);
		if(userHasPermissionRemove(user,f)){
			if(f.isDir()){
           			Dir dir = (Dir) f;
            			if (dir.getFileCount() > 0) {
                			dir.RmInsideDir(user);
					dir.remove();
            			}
           			else {
                			dir.remove();
           			}	
        		}
        		else {
        			f.remove();	
			}
		}else{
			throw new PermissionDeniedException();
		}
	}else{
		throw new NoSuchFileOrDirectoryException(name);
	}
    }

    /**
     *	File remover Inside Dir : remove everything inside and then itself except if it finds a Dir, then it proceeds differently
     *  @param	user        Current user
     */
    public void RmInsideDir(User user) throws PermissionDeniedException{	
        for(File f_inside : getFileSet()){
            	if(userHasPermissionRemove(user,f_inside)){
            		if(f_inside.isDir()){
           		    	Dir dir = (Dir) f_inside;
				if (dir.getFileCount() > 0) {
                			dir.RmInsideDir(user);
					dir.remove();
            			}
           			else {
                			dir.remove();
           			}
           		 }
            		 else{
             	  		f_inside.remove();
           		 }
	   	}	
        }
    }
     

     /**
      * Get the absolute path of the Dir
      * @return path        Dir path
      */
	public String absolutePath(){
	String path = getName();
	if(path.equals(getParent().getName()))
		return path;
	else 
		return (path + "/" + getParent().absolutePath());
	}
    
    /**
     *  xmlImport function
     *  @param pfileElement     list of files to import for this user
     */
    public void xmlImport(Element dirElement) throws ImportDocumentException {
    	super.xmlImport(dirElement);
    }
    

    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setName("Dir");
        for (File f: getFileSet())
                element.addContent(f.xmlExport());
            return element;
        }

    @Override
    public boolean isDir(){
        return true;
    }

    @Override
    public boolean isFile(){
        return false;
    }


}
