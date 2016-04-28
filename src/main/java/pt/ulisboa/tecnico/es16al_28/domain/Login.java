package pt.ulisboa.tecnico.es16al_28.domain;

import java.util.*;
import java.math.BigInteger;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;

import pt.ulisboa.tecnico.es16al_28.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.IncorrectPasswordException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenExpiredException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotDirException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotLoginException;

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
        if (!user.checkPassword(password)) {
            throw new IncorrectPasswordException();
        }
        if (password.length() < 8) {
            throw new CannotLoginException();
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
     *  Special Login constructor for user Guest
     *  @param  mydrive     MyDrive application
     *  @param  username    User's username
     */
    public Login(String username) throws UserDoesNotExistException, IncorrectPasswordException{
        if (!username.equals("nobody")) {
            throw new IncorrectPasswordException();
        }
        MyDrive mydrive = MyDrive.getInstance();
        User user = mydrive.getUserByUsername(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
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
        if (username.equals("nobody")) {
            return true;
        }
        if (interval.toDurationMillis() > 3600000) {
            return false;
        }
        if (username.equals("root")) {
            if (interval.toDurationMillis() > 600000) {
                return false;
            }
            return true;
        }
        super.setValidity(now);
        return true;
    }

    /**
     *  Changes the current directory
     *  @param  name        directory name
     */

	/*
    public String cd(MyDrive mydrive ,String name) throws NoSuchFileOrDirectoryException, NotDirException {
		File currentDir;
		Dir cast;
		if(name.indexOf('/') == -1){
        	if (name == ".");
        	else if (name == "..")
        		setCurrentDir(getCurrentDir().getParent());
        	else {
            	currentDir = getCurrentDir().getFileByName(name);
            	if(currentDir.isDir()){
		    	cast = (Dir) currentDir;
		    	setCurrentDir(cast);
		    	}
		    	else throw new NotDirException(name);
		    }
		}
		else{
			String[] path = name.split("/");
			setCurrentDir(mydrive.getRootDir());
			for(String s: path)
				if(!s.equals("")){
					currentDir = getCurrentDir().getFileByName(name);
            			if(currentDir.isDir()){
		    			cast = (Dir) currentDir;
		    			setCurrentDir(cast);
		    		}
		    		else throw new NotDirException(name);
		    	}
		}
	return (getCurrentDir().absolutePath());
    }*/

	public String cd(String name) throws NoSuchFileOrDirectoryException, NotDirException {
		File currentFile;
		Dir cast;
		if(name.indexOf('/') == -1){
        		if (name == ".");
        		else if (name == "..")
        			super.setCurrentDir(getCurrentDir().getParent());
        		     else {
            			currentFile = getCurrentDir().getFileByName(name);
            			if(currentFile.isDir()){
		    			cast = (Dir) currentFile;
		   		 	super.setCurrentDir(cast);
		    		}
		    		else throw new NotDirException(name);
		    	     }
		}
		else{
			super.setCurrentDir(MyDrive.getInstance().getRootDir().getParent());
			if(name == "/"){
				return (getCurrentDir().absolutePath());
			}
			String p = name.substring(1);
			for(String v: p.split("/")){
				if (getCurrentDir().directoryHasFile(v)){
					currentFile = getCurrentDir().getFileByName(v);
            				if(currentFile.isDir()){
		    				cast = (Dir) currentFile;
		    				super.setCurrentDir(cast);
		    			}
		    			else throw new NotDirException(v);
				}
                else {
					if (v !="home" && getCurrentDir().getName() == "/")
						throw new NoSuchFileOrDirectoryException(v);
				}		
            }
		}
	return (getCurrentDir().absolutePath());
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
        throw new PermissionDeniedException();
    }

    
}
