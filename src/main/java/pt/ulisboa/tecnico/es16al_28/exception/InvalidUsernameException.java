package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidUsernameException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidUsernameException(String username) {
        super("The give username ('" + username + "') contains invalid characters");
    }
}