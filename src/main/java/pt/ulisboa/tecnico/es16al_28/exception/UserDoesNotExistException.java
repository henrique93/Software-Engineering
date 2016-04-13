package pt.ulisboa.tecnico.es16al_28.exception;

public class UserDoesNotExistException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public UserDoesNotExistException(String username) {
        super("Username '" + username + "' does not exist");
    }
}