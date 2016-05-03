package pt.ulisboa.tecnico.es16al_28.exception;

public class CannotLoginException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public CannotLoginException() {
        super("Users with passwords shorter than 8 digits cannot create new sessions");
    }
}