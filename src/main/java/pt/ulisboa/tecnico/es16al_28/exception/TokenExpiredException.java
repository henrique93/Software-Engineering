package pt.ulisboa.tecnico.es16al_28.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenExpiredException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    static final Logger log = LogManager.getRootLogger();

    public TokenExpiredException() {
        super("Your session has expired. Please relog to continue operating");
        log.warn("User of an expired token");
    }
}