package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class File extends File_Base {
    
    public File() {
        super();
    }
    
    /**
     *  file initializer
     */
    public void init(MyDrive mydrive, String name, String permission, User owner, Dir dir) {
        setId(mydrive.incID());
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission(permission);
        setOwner(owner);
        setDir(dir);
    }

     public void remove() {
      	setDir(null);
        deleteDomainObject();
    }

     public Element xmlExport() {
	Element element = new Element("File");
    	element.setAttribute("id", Integer.toString(getId()));
        element.setAttribute("name", getName());
        element.setAttribute("lastChange",  getLastChange());
        element.setAttribute("permission", getPermission());
	return element;
    }
	
    
    @Override
    public String toString() {
        return "File ID: " + getId() + "\tName: " + getName() + "\tOwner: " + getOwner().toString() + "\tPermisisons: " + getPermission() + "\tModified last at: " + getLastChange();
    }
}
