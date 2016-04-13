package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.IncorrectPasswordException;

public class LoginService extends MyDriveService {

    private long _token;
    private String _username;
    private String _password;

    public LoginService(String username, String password) {

        _username = username;
        _password = password;
    }

    public long result() {
        return _token;
    }

    @Override
    public final void dispatch() throws UserDoesNotExistException, IncorrectPasswordException {
        Login logged = new Login(_username, _password);
        _token = logged.getToken();
    }
}
