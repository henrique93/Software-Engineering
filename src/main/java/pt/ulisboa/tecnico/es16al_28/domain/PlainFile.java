package pt.ulisboa.tecnico.es16al_28.domain;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
    
    public PlainFile(int id, String name, String permission, String owner, Dir dir) {
        super();
        init(id, name, permission, owner, dir);
    }

}
