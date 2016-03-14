package pt.ulisboa.tecnico.es16al_28.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class File extends File_Base {
    
    public File() {
        super();
    }
    
    /**
     *  file initializer
     */
    public void init(int id, String name, String permission, String owner, Dir dir) {
        setId(id);
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

    
    
    @Override
    public String toString() {
        return "File ID: " + getId() + "\tName: " + getName() + "\tOwner: " + getOwner() + "\tPermisisons: " + getPermission() + "\tModified last at: " + getLastChange();
    }
}
