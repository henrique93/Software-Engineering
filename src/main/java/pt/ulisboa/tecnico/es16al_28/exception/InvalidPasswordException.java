package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidPasswordException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidPasswordException() {
        super("Invalid password: Password must be have at least 8 characters");
    }
}