package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.util.Set;
import java.util.Iterator;

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
     */
    public Dir(MyDrive mydrive) {
        super();
        setParent(this);
        init(mydrive, "/", "rwxdr-x-", mydrive.getSuperUser(), this);
    }

    /**
     *  Special constructor for directory "home"
     *  @param id
     *  @param dir  directory "/"
     */
    public Dir(MyDrive mydrive, Dir dir) {
        super();
        setParent(dir);
        init(mydrive, "home", "rwxdr-x-", mydrive.getSuperUser(), dir);
    }
    
    /**
     *  Common directory constructor
     */
    public Dir(MyDrive mydrive, String name, String permission, User owner, Dir dir) throws FileAlreadyExistsException {
        super();
        for (File file: getFileSet()) {
            if (file.getName() == name) {
                throw new FileAlreadyExistsException(name);
            }
        }
        setParent(dir);
        init(mydrive, name, permission, owner, dir);
    }
    
    
    
    
    public Dir(Dir dir, Element xml) throws ImportDocumentException {
        setDir(dir);
        xmlImport(xml);
    }
    
    /**
     *  Directory's simple listing
     */
    public String listDir() {
        String ls = ".\n..\n"; /* . e .. ou nomes??? */
        for (File file: getFileSet()) {
            ls += file.toString() + "\n";
        }
        return ls;
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
     *  Muda de directorio
     *  @return dir     directory to change to
     */
    public Dir cd(MyDrive mydrive, Dir currentDir, String arg) throws NoSuchFileOrDirectoryException {
        Set entries = currentDir.getFileSet();
        if (arg == ".") {
            return currentDir;
        }
        else if (arg == "..") {
            return currentDir.getParent();
        }
        else if (arg != null) {
            return currentDir; //FIXME
        }
        else {
            throw new NoSuchFileOrDirectoryException(arg);
        }
    }
    
    /**
     *  xmlImport function
     *  @param pfileElement     list of files to import for this user
     */
    public void xmlImport(Element pfileElement) throws ImportDocumentException {
        try {
            setId(pfileElement.getAttribute("id").getIntValue());
            setName(new String(pfileElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setLastChange(new String(pfileElement.getAttribute("lastChange").getValue().getBytes("UTF-8")));
            setPermission(new String(pfileElement.getAttribute("permission").getValue().getBytes("UTF-8")));
            /*setOwner(new User(pfileElement.getAttribute("owner").getValue().getBytes("UTF-8")));*/

        } catch (UnsupportedEncodingException | DataConversionException e) {
            throw new ImportDocumentException();
        }
    }

    public Element xmlExport() {
        Element element = super.xmlExport();
	element.setName("Dir");
	for (File f: getFileSet())
            element.addContent(f.xmlExport());
        return element; 
    }

    
}
