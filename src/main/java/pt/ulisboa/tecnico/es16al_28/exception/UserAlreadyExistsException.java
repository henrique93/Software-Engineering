package pt.ulisboa.tecnico.es16al_28.exception;

public class UserAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public UserAlreadyExistsException(String username) {
        super("Username '" + username + "' already in use");
    }
}