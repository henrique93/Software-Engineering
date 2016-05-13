package pt.ulisboa.tecnico.es16al_28.exception;

public class CyclicLinkException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public CyclicLinkException() {
        super("Can not perform actions over cyclic links");
    }

}