package pt.ulisboa.tecnico.es16al_28.exception;

public class FileNotFoundException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _fileName;
    
    public FileNotFoundException(String fileName) {
        _fileName = fileName;
    }
    
    public String getFileName() {
        return _fileName;
        
    }
    
    @Override
    public String getMessage() {
        return "File does not exist: " + _fileName;
    }
}
