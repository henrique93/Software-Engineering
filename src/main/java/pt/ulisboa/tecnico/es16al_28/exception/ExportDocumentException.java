package pt.ulisboa.tecnico.es16al_28.exception;

public class ExportDocumentException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public ExportDocumentException() {
        super("Error in importing document from XML");
    }
}