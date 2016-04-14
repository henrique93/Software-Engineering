package pt.ulisboa.tecnico.es16al_28.domain;

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

public class Login extends Login_Base {
    
    public Login() {
        super();
    }

    /**
     *  User constructor
     *  @param  mydrive     MyDrive application
     *  @param  username    User's username
     *  @param  password    User's password
     */
    public Login(String username, String password) throws UserDoesNotExistException, IncorrectPasswordException{
        MyDrive mydrive = MyDrive.getInstance();
        User user = mydrive.getUserByUsername(username);
        if (user.equals(null)) {
            throw new UserDoesNotExistException(username);
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectPasswordException();
        }
        setUser(user);
        setCurrentDir(user.getDir());
        long token = new BigInteger(64, new Random()).longValue();
        while (mydrive.isLogged(token)) {
            token = new BigInteger(64, new Random()).longValue();
        }
        setToken(token);
        setValidity(new DateTime());
        for (Login login : mydrive.getLogedSet()) {
            if(!login.CheckValidity(login.getToken())) {
                login.removeLogin();
            }
        }
        setMydriveL(mydrive);
    }

    /**
     *  Login destroyer
     *  Removes this login
     */
    public void removeLogin() {
        setMydriveL(null);
        setUser(null);
        setCurrentDir(null);
        setToken(null);
        setValidity(null);
        deleteDomainObject();
    }

    /**
     *  Check if the token is still valid
     *  @param  mydrive     MyDrive application
     *  @param  token       Login's token
     *  @return boolean     True if validity was refreshed under 2h from now, false otherwise
     */
    public boolean CheckValidity(Long token) throws TokenDoesNotExistException {
        DateTime now = new DateTime();
        Interval interval = new Interval(getValidity(), now);
        if (interval.toDurationMillis() > 3600000) {
            return false;
        }
        setValidity(now);
        return true;
    }

    /**
     *  Changes the current directory
     *  @param  name        directory name
     */
    public String cd(String name) throws NoSuchFileOrDirectoryException, NotDirException {
        MyDrive mydrive = MyDrive.getInstance();
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
    
}
