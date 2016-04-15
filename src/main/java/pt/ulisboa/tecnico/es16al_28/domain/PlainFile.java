package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class PlainFile extends PlainFile_Base {
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public PlainFile () {
        super();
    }
    
    /**
     *  PlainFile constructor
     *  @param  login       Current MyDrive login
     *  @param  name        PlainFile's name
     *  @param  app         PlainFile's content
     */
    public PlainFile(Login login, String name, String app) {
        super();
        init(login.getMydriveL(), name, login.getUser().getUmask(), login.getUser(), login.getCurrentDir());
        setApp(app);
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public PlainFile(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
    	initxml(mydrive, owner, dir,  xml);
    }
    
    /**
     *  Reads the content from a file
     *  @param  file        File to read the content from
     *  @return string      File's content
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
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public void xmlImport(Element plainfileElement) throws ImportDocumentException {
        
    	super.xmlImport(plainfileElement);

    	try {
            setApp(new String(plainfileElement.getAttribute("app").getValue().getBytes("UTF-8")));

	} catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
    }
    
    /**
     *  FALTA COMENTAR_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>_>
     */
    public Element xmlExport() {
        Element element = super.xmlExport();
        element.setAttribute("app", getApp());
	element.setName("PlainFile");
        return element; 
    }
}
