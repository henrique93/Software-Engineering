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
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;

public class Dir extends Dir_Base {
    
    /**
     *  Special constructor for directory "/"
     *  @param  mydrive     MyDrive application
     */
    public Dir(MyDrive mydrive) {
        super();
        initRoot(mydrive, "/", this);
    }

    /**
     *  Special constructor for directory "home"
     *  @param  mydrive     MyDrive application
     *  @param  dir         directory "/"
     */
    public Dir(MyDrive mydrive, Dir dir) throws InvalidNameException, PermissionDeniedException {
        super();
        initRoot(mydrive, "home", dir);
    }

    /**
     *  Special constructor for user's home directory
     *  @param  login       Current MyDrive login
     *  @param  name        Directory's name
     */
    public Dir(Login login, String name, String permission, User user) throws FileAlreadyExistsException, InvalidNameException, PermissionDeniedException {
        super();
        MyDrive mydrive = login.getMydriveL();
        for (File file: getFileSet()) {
            String fileName = file.getName();
            if (fileName.equals(name)) {
                throw new FileAlreadyExistsException(name);
            }
        }
        initHome(mydrive, name, permission, user);
    }

    /**
     *  Common directory constructor
     *  @param  login       Current MyDrive login
     *  @param  name        Directory's name
     */
    public Dir(Login login, String name) throws FileAlreadyExistsException, TooLongException, InvalidNameException, PermissionDeniedException {
        super();
        User user = login.getUser();
        for (File file: getFileSet()) {
            String fileName = file.getName();
            if (fileName.equals(name)) {
                throw new FileAlreadyExistsException(name);
            }
        }
        if (!inPathLimit(login, name)) {
            throw new TooLongException();
        }
        init(login.getMydriveL(), name, user.getUmask(), user, login.getCurrentDir());
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public Dir(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
    	initxml(mydrive, owner, dir,  xml);
    }


   /**
     *  Get File inside the current directory by name
     *  @param  name        Name of file
     *  @return file        File with the give name
     */

    public File getFileByName(String name) throws NoSuchFileOrDirectoryException {
        for (File file: getFileSet()) {
            String _name = file.getName();
            if (_name.equals(name)) {
                return file;
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
        for (File file: getFileSet()) {
            String _name = file.getName();
            if (_name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     *	File remover : remove especific file inside current Dir except if it finds a Dir, then it proceeds differently
     */
    public void removeFile(User user, String name) throws PermissionDeniedException, NoSuchFileOrDirectoryException{
        File f = getFileByName(name);
        f.remove(user);
    }

    /**
     *	File remover Inside Dir : remove everything inside and then itself except if it finds a Dir, then it proceeds differently
     *  @param	user        Current user
     */
    @Override
    public void remove(User user) throws PermissionDeniedException{	
        if(getFileCount() > 0){
            for(File f_inside : getFileSet()){
                f_inside.remove(user);
            }
        }
        setParent(null);
        setOwner(null);
        deleteDomainObject();	
    }
     

     /**
      * Get the absolute path of the Dir
      * @return path        Dir path
      */
	public String absolutePath(){
        String path = getName();
        if(path.equals(getParent().getName())) {
            return path;
        }
        else {
            return (path + "/" + getParent().absolutePath());
        }
    }
    
    /**
     *  xmlImport function
     *  @param pfileElement     list of files to import for this user
     */
    public void xmlImport(Element dirElement) throws ImportDocumentException {
    	super.xmlImport(dirElement);
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
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
