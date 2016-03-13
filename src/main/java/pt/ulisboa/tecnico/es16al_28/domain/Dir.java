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
    
}