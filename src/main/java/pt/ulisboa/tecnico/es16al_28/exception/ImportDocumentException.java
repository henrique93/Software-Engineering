package pt.ulisboa.tecnico.es16al_28.exception;

public class ImportDocumentException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public ImportDocumentException() {
        super("Error in importing document from XML");
    }
}