package pt.ulisboa.tecnico.es16al_28.exception;

public class InvalidExtensionException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidExtensionException(String appName, String extensionName) {
        super(appName + " cannot run " + extensionName + " extension");
    }
}
