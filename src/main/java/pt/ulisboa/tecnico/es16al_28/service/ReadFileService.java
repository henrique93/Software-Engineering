package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class ReadFileService extends MyDriveService {

    private long _token;
    private String _name;
    private String _result;

    public ReadFileService(long token, String name) {
    	_name = name;
    	_token = token;
    }

    @Override
    public final void dispatch() throws TokenDoesNotExistException, NoSuchFileOrDirectoryException, NotFileException, PermissionDeniedException {

        Login logged = MyDrive.getInstance().getLoginByToken(_token);
        File f = logged.getCurrentDir().getFileByName(_name);

        if(f.isDir()) {
            throw new NotFileException(_name);
        }
        else {
            PlainFile file = (PlainFile)f;
            _result = file.readFile(logged);
        }
    }

    public final String result() {
        return _result;
    }
}
