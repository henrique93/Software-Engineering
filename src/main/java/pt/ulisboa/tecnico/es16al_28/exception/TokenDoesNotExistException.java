package pt.ulisboa.tecnico.es16al_28.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenDoesNotExistException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;

    static final Logger log = LogManager.getRootLogger();

    public TokenDoesNotExistException() {
        super("This session does not exist");
        log.warn("User of an inexistant token");
    }
}