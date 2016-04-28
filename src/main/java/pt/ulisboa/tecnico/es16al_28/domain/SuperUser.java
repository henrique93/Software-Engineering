package pt.ulisboa.tecnico.es16al_28.domain;

public class SuperUser extends SuperUser_Base {
    
    public SuperUser(MyDrive mydrive) {
        super();
        init("root", "rootroot", "SuperUser", "rwxdr-x-", mydrive);
    }
    
}
