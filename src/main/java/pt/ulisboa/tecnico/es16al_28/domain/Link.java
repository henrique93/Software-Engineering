package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

public class Link extends Link_Base {
    
    public Link(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        super();
        init(id, name, lastChange, permission, owner, dir);
    }
    
}
