package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;

import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.File;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

import java.io.*;

public class ExecuteFileService extends MyDriveService {

    private long _token;
    private String _path;
    private String[] _args;

    public ExecuteFileService (long token, String path, String[] args) {
    		_token = token;
    		_path = path;
    		_args = args;
    }

    
    @Override
    public final void dispatch() throws TokenDoesNotExistException, NoSuchFileOrDirectoryException{
    	MyDrive mydrive = getMyDrive();
    	Login l = mydrive.getLoginByToken(_token);
    	Dir currdir = l.getCurrentDir();
        File f;
		int pos;
        if ((pos = _path.lastIndexOf('/')) < 0) {
            f = currdir.getFileByName(_path);
        }
        else {
            f = l.getFileByPath(_path);
        }
        if (!f.hasPermission(l, "x")) {
            throw new PermissionDeniedException();
        }
        if(f.isApp()) {
            App appfile = (App) f;
            String method = appfile.readFile(l);
            try{
                appfile.run(method, _args);
            }
            catch (Exception e) { throw new RuntimeException(""+e); }
        }
        if(f.isPlainFile()) {
            PlainFile plainFile = (PlainFile) f;
            String method = plainFile.readFile(l);
            try{
                plainFile.run(_args);
            }
            catch (Exception e) { throw new RuntimeException(""+e); }
        }
        if(f.isLink()) {
            Link linkFile = (Link) f;
            String method = linkFile.readFile(l);
            PlainFile file = (PlainFile) l.getFileByPath(method);
            try{
                file.run(_args);
            }
            catch (Exception e) { throw new RuntimeException(""+e); }
        }
       
    }
}
