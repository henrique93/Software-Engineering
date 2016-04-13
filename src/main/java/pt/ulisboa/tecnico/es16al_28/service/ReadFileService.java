package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;

public class ReadFileService extends MyDriveService {

    private String _name;
    private Long _token;
    private String _result;

    public ReadFileService(String name, Long token) {
    	_name = name;
    	_token = token;
    }

    @Override
    public final void dispatch() throws TokenDoesNotExistException, NoSuchFileOrDirectoryException, NotFileException {

    	MyDrive md = getMyDrive();
        Login l = md.getLoginByToken(_token);


        File f = l.getCurrentDir().getFileByName(_name);

        if(f.isDir()) {
            throw new NotFileException(_name);
        }
        else {
            PlainFile a =(PlainFile) f;
            _result = a.readFile();
        }
    }

    public final String result() {
        return _result;
    }
}
