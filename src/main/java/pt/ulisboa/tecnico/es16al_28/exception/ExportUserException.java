package pt.ulisboa.tecnico.es16al_28.exception;

public class ExportUserException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public ExportUserException() {
        super("Error in exporting user from XML");
    }
}