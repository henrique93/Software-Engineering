package pt.ulisboa.tecnico.es16al_28.exception;

public class CannotCreateWithContentException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public CannotCreateWithContentException() {
        super("This type of file can not be created with content");
    }

}