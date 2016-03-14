package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

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
    
    
    public String listDir() {
        String ls = ".\n..\n"; 
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
            setOwner(new String(pfileElement.getAttribute("owner").getValue().getBytes("UTF-8")));

        } catch (UnsupportedEncodingException | DataConversionException e) {
            throw new ImportDocumentException();
        }
    }
    

}
