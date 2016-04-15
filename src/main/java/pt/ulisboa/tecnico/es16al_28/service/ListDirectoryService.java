package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import java.util.*;


public class ListDirectoryService extends MyDriveService {

    private long _token;
    private List<String> _listDirectory;

    public ListDirectoryService(Long token) {
        _token = token;
    }


    @Override
    public final void dispatch() throws TokenDoesNotExistException{
        MyDrive mydrive = getMyDrive();
        _listDirectory  = new ArrayList<String>();
    
        Login l = mydrive.getLoginByToken(_token);
        
        _listDirectory = l.getCurrentDir().listD();
        
    }
    
    public final List<String> result(){
        return _listDirectory;
    }

}
