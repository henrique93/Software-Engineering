package pt.ulisboa.tecnico.es16al_28.exception;

public class IncorrectPasswordException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}