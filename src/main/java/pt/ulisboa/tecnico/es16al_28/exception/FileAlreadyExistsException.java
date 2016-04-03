package pt.ulisboa.tecnico.es16al_28.exception;

public class FileAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _fileName;
    
    public FileAlreadyExistsException(String fileName) {
        _fileName = fileName;
    }
    
    public String getFileName() {
        return _fileName;
        
    }
    
    @Override
    public String getMessage() {
        return "A file named '" + _fileName + "' already exists in this directory";
    }
}
