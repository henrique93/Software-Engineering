package pt.ulisboa.tecnico.es16al_28.domain;


import org.jdom2.Element;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import org.jdom2.DataConversionException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
    
    public PlainFile(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        super();
        init(id, name, lastChange, permission, owner, dir);
    }

    public PlainFile(User owner, Dir dir, Element xml) throws ImportDocumentException {
        setOwner(owner);
        setDir(dir);
        xmlImport(xml);
    }
    
    
     /**
     *  Reads the content from a file
     *  @param file     file to read the content from
     *  @return string  file's content
     */
    public String readFile(PlainFile file) {
        return file.getApp();
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
