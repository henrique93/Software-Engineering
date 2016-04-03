package pt.ulisboa.tecnico.es16al_28.exception;

public class PermissionDeniedException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public PermissionDeniedException() {
        super();
    }
    
    @Override
    public String getMessage() {
        return "You do not have permission to perform this action";
    }
}
