package pt.ulisboa.tecnico.es16al_28.exception;

public class NotEmptyException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _dirName;
    
    public NotEmptyException(String dirName) {
        _dirName = dirName;
    }
    
    public String getDirName() {
        return _dirName;
        
    }
    
    @Override
    public String getMessage() {
        return "This directory is not empty: " + _dirName;
    }
}