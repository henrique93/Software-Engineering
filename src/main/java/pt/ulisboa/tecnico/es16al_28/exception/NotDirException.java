package pt.ulisboa.tecnico.es16al_28.exception;

public class NotDirException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _dirName;
    
    public NotDirException(String dirName) {
        _dirName = dirName;
    }
    
    public String getDirName() {
        return _dirName;
        
    }
    
    @Override
    public String getMessage() {
        return _dirName + " is not a directory";
    }
}
