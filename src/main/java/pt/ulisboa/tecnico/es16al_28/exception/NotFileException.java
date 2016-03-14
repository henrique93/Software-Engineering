package pt.ulisboa.tecnico.es16al_28.exception;

public class NotFileException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _fileName;
    
    public NotFileException(String fileName) {
        _fileName = fileName;
    }
    
    public String getFileName() {
        return _fileName;
        
    }
    
    @Override
    public String getMessage() {
        return _fileName + " is not a file";
    }
}