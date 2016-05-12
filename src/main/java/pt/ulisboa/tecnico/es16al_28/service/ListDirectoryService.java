package pt.ulisboa.tecnico.es16al_28.service;

import java.util.*;

import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.service.dto.FileDto;

public class ListDirectoryService extends MyDriveService {

    private long _token;
    private List<FileDto> _listDirectory;

    public ListDirectoryService(long token) {
        _token = token;
    }


    @Override
    public final void dispatch() throws TokenDoesNotExistException, PermissionDeniedException {
        MyDrive mydrive = getMyDrive();
        _listDirectory  = new ArrayList<FileDto>();
        Login l = mydrive.getLoginByToken(_token);
        ArrayList<File> list = l.ls();
        for (File f: list) {
            User owner = f.getOwner();
            FileDto Dto = new FileDto(f.getId(), f.getName(), owner.getUsername(), f.getPermission(), f.getLastChange());
            _listDirectory.add(Dto);
        }
    }
    
    public final List<FileDto> result() {
        return _listDirectory;
    }


}
