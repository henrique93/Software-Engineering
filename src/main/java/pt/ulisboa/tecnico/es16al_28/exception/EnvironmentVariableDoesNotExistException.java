package pt.ulisboa.tecnico.es16al_28.exception;

public class EnvironmentVariableDoesNotExistException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _evName;
    
    public EnvironmentVariableDoesNotExistException(String evName) {
        _evName = evName;
    }
    
    public String getEvName() {
        return _evName;
        
    }
    
    @Override
    public String getMessage() {
        return "Environment variable does not exist: " + _evName;
    }
}
