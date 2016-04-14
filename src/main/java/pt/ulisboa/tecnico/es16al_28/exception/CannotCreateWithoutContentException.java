package pt.ulisboa.tecnico.es16al_28.exception;

public class CannotCreateWithoutContentException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public CannotCreateWithoutContentException() {
        super("This type of file can not be created without content");
    }

}