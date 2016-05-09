package pt.ulisboa.tecnico.es16al_28.domain;

import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable(Login login, String name, String value) {
        super();
        super.setLogin(login);
        super.setName(name);
        super.setValue(value);
    }

    public void updateEv(String value) {
        super.setValue(value);
    }

    /* OVERRIDE SETTERS FOR SECURITY */
    @Override
    public void setLogin(Login login) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setName(String name) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setValue(String value) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }
    
}
