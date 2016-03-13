package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.util.Set;
import java.util.Iterator;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.FileNotFoundException;
import pt.ulisboa.tecnico.es16al_28.exception.NotEmptyException;

public class Dir extends Dir_Base {
    
    public Dir(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        super();
        init(id, name, lastChange, permission, owner, dir);
    }
   
    public Dir(User owner, Dir dir, Element xml) throws ImportDocumentException {
        setOwner(owner);
        setDir(dir);
        xmlImport(xml);
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
            throw new FileNotFoundException();
        }
    }
    
    /**
     *  Auxiliar function to rm
     *  @param dir      directory to be removed
     */
     
    public void removeDir(Dir dir) throws NotEmptyException {
        if (dir.getFileCount() > 0) {    /* >0 ou >2 ??? */
            throw new NotEmptyException();  /* Exception manda o conteudo??? */
        }
        else {
            removeFile(dir);
        }
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

    public void xmlImport(Element pfileElement) throws ImportDocumentException {
        try {
            setId(pfileElement.getAttribute("id").getIntValue());
            setName(new String(pfileElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setLastChange(new String(pfileElement.getAttribute("lastChange").getValue().getBytes("UTF-8")));
            setPermission(new String(pfileElement.getAttribute("permission").getValue().getBytes("UTF-8")));

        } catch (UnsupportedEncodingException | DataConversionException e) {
            throw new ImportDocumentException();
        }
    }

 
}
