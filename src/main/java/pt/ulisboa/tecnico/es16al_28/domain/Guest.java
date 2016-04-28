package pt.ulisboa.tecnico.es16al_28.domain;

public class Guest extends Guest_Base {
    
    public Guest(MyDrive mydrive) {
        super();
        init("nobody", null, "Guest", "rwxdr-x-", mydrive);
    }

    @Override
    public boolean checkPassword(String password) {
        return true;
    }

}
