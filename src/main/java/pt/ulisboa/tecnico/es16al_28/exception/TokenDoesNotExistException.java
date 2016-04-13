package pt.ulisboa.tecnico.es16al_28.exception;

public class TokenDoesNotExistException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public TokenDoesNotExistException() {
        super("This session does not exist");
    }
}