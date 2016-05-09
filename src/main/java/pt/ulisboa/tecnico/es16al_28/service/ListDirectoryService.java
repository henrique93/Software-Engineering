 package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import java.util.*;
import pt.ulisboa.tecnico.es16al_28.service.dto.FileDto;

public class ListDirectoryService extends MyDriveService {

    private long _token;
    private List<FileDto> _listDirectory;

    public ListDirectoryService(long token) {
        _token = token;
    }


    @Override
    public final void dispatch() throws TokenDoesNotExistException, PermissionDeniedException{
        MyDrive mydrive = getMyDrive();
        _listDirectory  = new ArrayList<FileDto>();
        Login l = mydrive.getLoginByToken(_token);
        
        Dir d = l.getCurrentDir();
        Dir p = d.getParent();
        
        if (d.listPermission(l) || p.listPermission(l)){

            FileDto Dto = new FileDto(d.getId(), d.getName(), d.getOwner().getUsername(), d.getPermission(), d.getLastChange());
            _listDirectory.add(Dto);
            Dto = new FileDto(p.getId(), p.getName(), p.getOwner().getUsername(), p.getPermission(), p.getLastChange());
            _listDirectory.add(Dto);
        
            for (File f : d.getFileSet()){
                Dto = new FileDto(f.getId(), f.getName(), f.getOwner().getUsername(), f.getPermission(), f.getLastChange());
                _listDirectory.add(Dto);
            }
        }else{
            throw new PermissionDeniedException();
        }
    }
    
    public final List<FileDto> result(){
        return _listDirectory;
    }


}
