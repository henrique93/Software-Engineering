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
    public void init(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        setId(id);
        setName(name);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setLastChange(timeStamp);
        setPermission(permission);
        setOwner(owner);
        setDir(dir);
    }
    
    
    @Override
    public String toString() {
        return "File ID: " + getId() + "Name: " + getName() + "Owner: " + getOwner().toString() + "Permisisons: " + getPermission() + "Modified last at: " + getLastChange();
    }
}
