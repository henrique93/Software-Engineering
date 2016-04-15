package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;

public class ChangeDirectoryService extends MyDriveService {

	private Long _token;
	private String _name;
	private String _path;

    public ChangeDirectoryService(Long token, String name) {
        _token = token;
        _name = name;
    }


    @Override
    public final void dispatch() throws NoSuchFileOrDirectoryException, TokenDoesNotExistException{
	MyDrive mydrive = getMyDrive();
        Login l = mydrive.getLoginByToken(_token);
	_path = l.cd(_name);
    }

    public final String result(){
        return _path;
    }

}
