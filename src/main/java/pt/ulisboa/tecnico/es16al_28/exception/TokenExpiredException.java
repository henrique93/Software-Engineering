package pt.ulisboa.tecnico.es16al_28.exception;

public class TokenExpiredException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public TokenExpiredException() {
        super("Your session has expired. Please relog to continue operating");
    }
}