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
     *  Remove a file or an empty directory from the current directory
     *  @param file     file or directory to be removed
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
                removeDir(dir);
            }
            else {
                removeFile(file);
            }
        }
        else {
            throw new FileNotFoundException(file.getName());
        }
    }
    
    /**
     *  Auxiliar function to rm
     *  @param dir      directory to be removed
     */
    public void removeDir(Dir dir) throws NotEmptyException {
        if (dir.getFileCount() > 1) {    /* >0 ou >2 ??? */
            throw new NotEmptyException(dir.getName());  /* Exception manda o conteudo??? */
        }
        else {
            removeFile(dir);
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
