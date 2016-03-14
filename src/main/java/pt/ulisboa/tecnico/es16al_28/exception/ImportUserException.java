package pt.ulisboa.tecnico.es16al_28.exception;

public class ImportUserException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public ImportUserException() {
        super("Error in importing user from XML");
    }
}