package pt.ulisboa.tecnico.es16al_28.exception;

public class NoExtensionException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public NoExtensionException(String fileName) {
        super(fileName + " has no extension associated.");
    }
}
