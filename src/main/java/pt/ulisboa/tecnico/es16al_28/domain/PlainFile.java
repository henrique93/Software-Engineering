package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
 
    public PlainFile(MyDrive mydrive, String name, String permission, User owner, Dir dir , String app) {
        super();
	setApp(app);
        init(mydrive, name, permission, owner, dir);
    }

    public PlainFile(Dir dir, Element xml) throws ImportDocumentException {
        setDir(dir);
        xmlImport(xml);
    }
  
   
    /**
     *  Reads the content from a file
     *  @param file     file to read the content from
     *  @return string  file's content
     */
    public String readFile(Login l) throws PermissionDeniedException{
        if(l.getUser().getUmask().charAt(4) == 'r' && getPermission().charAt(4) == l.getUser().getUmask().charAt(4)) {
            return getApp();
        }
        else {
            throw new PermissionDeniedException();
        }
    }


    /**
     *  Writes content in a file
     *  @param  file        File to write the content to
     *  @return string      Content to write
     */
    public void writeFile(Login l,String app) throws PermissionDeniedException {
         if(l.getUser().getUmask().charAt(5) == 'w' && getPermission().charAt(5) == l.getUser().getUmask().charAt(5)) {
             setApp(app);
         }
         else {
             throw new PermissionDeniedException();
         }
    }


  
    
    public void xmlImport(Element pfileElement) throws ImportDocumentException {
    try {
            setId(pfileElement.getAttribute("id").getIntValue());
            setName(new String(pfileElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setLastChange(new String(pfileElement.getAttribute("lastChange").getValue().getBytes("UTF-8")));
            setPermission(new String(pfileElement.getAttribute("permission").getValue().getBytes("UTF-8")));
            /*setOwner(new String(pfileElement.getAttribute("owner").getValue().getBytes("UTF-8")));*/

    } catch (UnsupportedEncodingException | DataConversionException e) {
            throw new ImportDocumentException();
        }
    }
    
     public Element xmlExport() {
        Element element = super.xmlExport();
        element.setAttribute("app", getApp());
	element.setName("PlainFile");
        return element; 
    }
}
