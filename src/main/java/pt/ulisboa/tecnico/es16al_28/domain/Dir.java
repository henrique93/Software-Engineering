package pt.ulisboa.tecnico.es16al_28.domain;

import java.util.Set;
import java.util.Iterator;

/* Import exceptions */
import pt.ulisboa.tecnico.es16al_28.exception.FileNotFoundException;
import pt.ulisboa.tecnico.es16al_28.exception.NotEmptyException;

public class Dir extends Dir_Base {
    
    public Dir(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        super();
        init(id, name, lastChange, permission, owner, dir);
    }
   
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
    
    public void removeDir(Dir dir) throws NotEmptyException {
        if (dir.getFileCount() > 0) {    /* >0 ou >2 ??? */
            throw new NotEmptyException();  /* Exception manda o conteudo??? */
        }
        else {
            removeFile(dir);
        }
    }

 
}
