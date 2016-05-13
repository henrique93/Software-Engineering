package pt.ulisboa.tecnico.es16al_28.domain;

import java.util.*;
import java.math.BigInteger;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import pt.ulisboa.tecnico.es16al_28.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.IncorrectPasswordException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenExpiredException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotDirException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotLoginException;
import pt.ulisboa.tecnico.es16al_28.exception.EnvironmentVariableDoesNotExistException;

public class Login extends Login_Base {
    
    public Login() {
        super();
    }

    /**
     *  Login constructor
     *  @param  mydrive     MyDrive application
     *  @param  username    User's username
     *  @param  password    User's password
     */
    public Login(String username, String password) throws UserDoesNotExistException, IncorrectPasswordException{
        MyDrive mydrive = MyDrive.getInstance();
        User user = mydrive.getUserByUsername(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        if (!username.equals("nobody")) {
            if (!user.checkPassword(password)) {
                throw new IncorrectPasswordException();
            }
            if (password.length() < 8) {
                throw new CannotLoginException();
            }
        }
        super.setUser(user);
        super.setCurrentDir(user.getDir());
        long token = new BigInteger(64, new Random()).longValue();
        while (mydrive.isLogged(token)) {
            token = new BigInteger(64, new Random()).longValue();
        }
        super.setToken(token);
        super.setValidity(new DateTime());
        if (mydrive.getLogedCount() > 0) {
            for (Login login : mydrive.getLogedSet()) {
                if ((login.getUser() != null) && !login.CheckValidity()) {    //VERIFICACAO DO USER=NULL COMO E QUE PODE ESTAR NULL???
                    login.removeLogin();
                }
            }
        }
        super.setMydriveL(mydrive);
    }

    /**
     *  Login remover
     *  Removes this login
     */
    public void removeLogin() {
        super.setMydriveL(null);
        super.setUser(null);
        super.setCurrentDir(null);
        super.setToken(null);
        super.setValidity(null);
        deleteDomainObject();
    }

    /**
     *  Check if the login's token is still valid and if so, refreshes validity
     *  @return boolean     True if validity was refreshed under 2h from now, false otherwise
     */
    public boolean CheckValidity() throws TokenDoesNotExistException {
        String username = getUser().getUsername();
        DateTime now = new DateTime();
        Interval interval = new Interval(getValidity(), now);
        long duration = interval.toDurationMillis();
        if (username.equals("nobody")) {
            return true;
        }
        if (duration > 3600000) {
            return false;
        }
        if (username.equals("root")) {
            if (duration > 600000) {
                return false;
            }
            return true;
        }
        super.setValidity(now);
        return true;
    }


    /**
     *  Get File by absolute path
     *  @param  path        absolute path to the file
     *  @return file        File with the given path
     */
    public File getFileByPath(String path) throws NoSuchFileOrDirectoryException {
        Dir fileDir = getCurrentDir();
        String fileName = path;
        if (path.contains("/")) {
            String cdwPath = getCurrentDir().absolutePath();
            String pathToDir = path.substring(0,path.lastIndexOf("/"));
            cd(pathToDir);
            fileDir = getCurrentDir();
            fileName = path.substring(path.lastIndexOf("/") + 1).trim();
        }
        for (File file: fileDir.getFileSet()) {
            String _name = file.getName();
            if (_name.equals(fileName)) {
                return file;
            }
        }
        throw new NoSuchFileOrDirectoryException(path);
    }


    /**
     *  Get EnvironmentVariable by name
     *  @param  name        			Environment variable's name
     *  @return EnvironmentVariable		EnvironmentVariable with the given
     */
    public EnvironmentVariable getEvByName(String name) throws EnvironmentVariableDoesNotExistException {
        for (EnvironmentVariable ev : getEnvVarSet()) {
            String evName = ev.getName();
            if (evName.equals(name)) {
                return ev;
            }
        }
        throw new EnvironmentVariableDoesNotExistException(name);
    }


    /**
     *  Lists the current directory's files
     *  @return ArrayList<File>     A list with every file in the current directory (including it's parent)
     */
    public ArrayList<File> ls() {
        ArrayList<File> _listDirectory  = new ArrayList<File>();
        Dir cwd = getCurrentDir();
        if (cwd.hasPermission(this, "r")) {
            File file = cwd;
            _listDirectory.add(file);
            file = cwd.getParent();
            _listDirectory.add(file);
            for (File f : cwd.getFileSet()) {
                _listDirectory.add(f);
            }
            return _listDirectory;
        }
        else {
            throw new PermissionDeniedException();
        }
    }


    /**
     *  Changes the current directory
     *  @param  name        Path or name to the new directory
     *  @return String      New directory's current path
     */
	public String cd(String name) throws NoSuchFileOrDirectoryException, NotDirException, PermissionDeniedException {
        File currentFile;
		Dir newDir;
		if(name.indexOf('/') == -1) {
            if (name == ".");
            else if (name == "..") {
                newDir = getCurrentDir().getParent();
                if (newDir.hasPermission(this, "x")) {
                    super.setCurrentDir(newDir);
                }
                else {
                    throw new PermissionDeniedException();
                }
            }
            else {
                currentFile = getCurrentDir().getFileByName(name);
                if(currentFile.isDir()){
                    newDir = (Dir) currentFile;
                    if (newDir.hasPermission(this, "x")) {
                        super.setCurrentDir(newDir);
                    }
                    else {
                        throw new PermissionDeniedException();
                    }
                }
                else {
                    throw new NotDirException(name);
                }
            }
		}
		else {
            MyDrive mydrive = MyDrive.getInstance();
            Dir root = mydrive.getRootDir();
			super.setCurrentDir(root.getParent());
			if(name == "/") {
				return (getCurrentDir().absolutePath());
			}
			String p = name.substring(1);
			for(String v: p.split("/")) {
				if (getCurrentDir().directoryHasFile(v)) {
					currentFile = getCurrentDir().getFileByName(v);
                    if(currentFile.isDir()) {
                        newDir = (Dir) currentFile;
                        if (newDir.hasPermission(this, "x")) {
                            super.setCurrentDir(newDir);
                        }
                        else {
                            throw new PermissionDeniedException();
                        }
                    }
                    else {
                        throw new NotDirException(v);
                    }
				}
                else {
						throw new NoSuchFileOrDirectoryException(v);
				}		
            }
		}
        return getCurrentDir().absolutePath();
    }


    /**
     *  Login's description
     *  Example:
     *  Name: user     Username: username      Umask: rwxdr-x-
     */
    @Override
    public String toString() {
        return getUser().toString();
    }




    /* OVERRIDE SETTERS FOR SECURITY */
    @Override
    public void setMydriveL(MyDrive mydrive) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setUser(User user) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setCurrentDir(Dir dir) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setToken(java.lang.Long token) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setValidity(DateTime dateTime) throws PermissionDeniedException {
        if (!dateTime.isAfter(getValidity())) {
            super.setValidity(dateTime);
        }
        else {
            throw new PermissionDeniedException();
        }
    }

    
}
