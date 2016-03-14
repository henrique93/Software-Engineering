package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
    
    public PlainFile(int id, String name, String permission, String owner, Dir dir) {
        super();
        init(id, name, permission, owner, dir);
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
    public String readFile() {
        return this.getApp();
    }

    public void xmlImport(Element pfileElement) throws ImportDocumentException {
    try {
            setId(pfileElement.getAttribute("id").getIntValue());
            setName(new String(pfileElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setLastChange(new String(pfileElement.getAttribute("lastChange").getValue().getBytes("UTF-8")));
            setPermission(new String(pfileElement.getAttribute("permission").getValue().getBytes("UTF-8")));
            setOwner(new String(pfileElement.getAttribute("owner").getValue().getBytes("UTF-8")));

    } catch (UnsupportedEncodingException | DataConversionException e) {
            throw new ImportDocumentException();
        }
    }

}
