package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidNameException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidNameException(String name) {
        super("The given name ('" + name + "') contains invalid characters");
    }
}