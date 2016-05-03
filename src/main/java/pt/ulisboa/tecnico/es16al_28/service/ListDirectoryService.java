package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import java.util.*;
import pt.ulisboa.tecnico.es16al_28.service.dto.ListDto;

public class ListDirectoryService extends MyDriveService {

    private long _token;
    private List<ListDto> _listDirectory;
     //private List<String> _listDirectory;

    public ListDirectoryService(Long token) {
        _token = token;
    }


    @Override
    public final void dispatch() throws TokenDoesNotExistException{
        MyDrive mydrive = getMyDrive();
        _listDirectory  = new ArrayList<ListDto>();
    
        Login l = mydrive.getLoginByToken(_token);
        Dir d = l.getCurrentDir();
        Dir p = d.getParent();
        
        _listDirectory.add(new ListDto(d.getId(), d.getName(), d.getOwner().toString(), d.getPermission(), d.getLastChange()));
        
        _listDirectory.add(new ListDto(p.getId(), p.getName(), p.getOwner().toString(), p.getPermission(), p.getLastChange()));
        
        for (File f : d.getFileSet()){
            _listDirectory.add(new ListDto(f.getId(), f.getName(), f.getOwner().toString(), f.getPermission(), f.getLastChange()));
        }
                                
       
    }
    
    public final List<ListDto> result(){
        return _listDirectory;
    }

}
