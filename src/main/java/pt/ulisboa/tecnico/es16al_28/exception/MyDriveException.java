package pt.ulisboa.tecnico.es16al_28.exception;

public abstract class MyDriveException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public MyDriveException() {
    }
    
    public MyDriveException(String msg) {
        super(msg);
    }
}