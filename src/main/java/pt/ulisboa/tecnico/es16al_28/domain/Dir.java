package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.util.Set;
import java.util.Iterator;

/* Import exceptions */
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.FileNotFoundException;
import pt.ulisboa.tecnico.es16al_28.exception.NotEmptyException;

public class Dir extends Dir_Base {
    
    public Dir() {
        super();
        setSelf(this);
        setParent(this);
        init(0, "/","rwxdr-x-", "root", this);
    }

    public Dir(int id, String name, String permission, Dir dir) {
        super();
        setSelf(this);
        setParent(dir);
        init(id, name, permission, "root", dir);
    }
    
    public Dir(int id, String name, String permission, String owner, Dir dir) {
        super();
        setSelf(this);
        setParent(dir);
        init(id, name, permission, owner, dir);
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
     
    public void rm(File file) throws FileNotFoundException, NotEmptyException {
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
        if (dir.getFileCount() > 0) {    /* >0 ou >2 ??? */
            throw new NotEmptyException(dir.getName());  /* Exception manda o conteudo??? */
        }
        else {
            removeFile(dir);
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
            setOwner(new String(pfileElement.getAttribute("owner").getValue().getBytes("UTF-8")));

        } catch (DataConversionException e) {
            throw new ImportDocumentException();
        }
    }

    public Element xmlExport() {
        Element element = new Element("dir");
        element.setAttribute("id", Integer.toString(getId()));
        element.setAttribute("name", getName());
        element.setAttribute("lastChange",  getLastChange());
        element.setAttribute("permission", getPermission());
        element.setAttribute("owner", getOwner());
        
        return element; 
    }

    
}
