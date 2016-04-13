package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidTypeException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidTypeException(String type) {
        super("The given type ('" + type + "') does not exist");
    }
}
