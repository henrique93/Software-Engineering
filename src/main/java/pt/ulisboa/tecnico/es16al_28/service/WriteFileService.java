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

public class WriteFileService extends MyDriveService {

	private long _token;
    private String _name;
    private String _content;

    public WriteFileService(long token, String name, String content) {
    	_token = token;
    	_name = name;
    	_content = content;
    }

    @Override
    public final void dispatch() throws TokenDoesNotExistException, NoSuchFileOrDirectoryException, NotFileException, PermissionDeniedException  {

        Login logged = MyDrive.getInstance().getLoginByToken(_token);

        File f = logged.getCurrentDir().getFileByName(_name);

        if(f.isDir()) {
            throw new NotFileException(_name);
        }
        else {
            PlainFile file = (PlainFile)f;
            file.writeFile(logged, _content);
        } 
    }
}
