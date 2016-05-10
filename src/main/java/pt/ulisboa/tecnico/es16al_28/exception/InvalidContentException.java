package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidContentException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidContentException(String content) {
        super("The given content ('" + content + "') contains invalid characters");
    }
}