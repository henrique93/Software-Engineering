package pt.ulisboa.tecnico.es16al_28.exception;

public class TooLongException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public TooLongException() {
        super();
    }
    
    @Override
    public String getMessage() {
        return "The path for this directory is too long. Give it a shorter name or create it in a different directory";
    }
}
