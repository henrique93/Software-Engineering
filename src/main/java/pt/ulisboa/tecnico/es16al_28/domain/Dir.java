package pt.ulisboa.tecnico.es16al_28.domain;

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
    

}
