package pt.ulisboa.tecnico.es16al_28.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.exception.MyDriveException;

public abstract class MyDriveService {
    protected static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    static MyDrive getMyDrive() {
        return MyDrive.getInstance();
    }

    /* static Person getPerson(String personName) throws PersonDoesNotExistException {
        Person p = getMyService().getPersonByName(personName);

        if (p == null)
            throw new PersonDoesNotExistException(personName);

        return p;
    }*/

    protected abstract void dispatch() throws MyDriveException;
}
