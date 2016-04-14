package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.MyDriveException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.File;

public class DeleteFileService extends MyDriveService {

    private long _token;
    private String _name;

    public DeleteFileService(String name, long token) {
        _token = token;
        _name = name;
    }
	/*TokenDoesNotExistException, PermissionDeniedException , NoSuchFileOrDirectoryException*/

    @Override
    public final void dispatch() throws MyDriveException{
        MyDrive mydrive = getMyDrive();
        Login l = mydrive.getLoginByToken(_token);
        Dir dir = l.getCurrentDir();
	dir.removeFile(l.getUser(),_name);	
    }

}
