package pt.ulisboa.tecnico.es16al_28.domain;

import pt.ulisboa.tecnico.es16al_28.exception.UserAlreadyExistsException;

public class User extends User_Base {
    
    public User(String username, String password, String name, String umask, Dir home, MyDrive mydrive) throws UserAlreadyExistsException {
        for (User u: mydrive.getUserSet()) {
            if (u.getUsername() == username) {
                throw new UserAlreadyExistsException();
            }
        }
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
        setDir(home);
        setMydrive(mydrive);
	
    }

    /**
     *  Super User special constructor
     */
    public User(MyDrive mydrive, Dir parent, int dirID) {
        Dir dirRoot;
        setUsername("root");
        setPassword("rootroot");
        setName("Super User");
        setUmask("rwxdr-x-");
        setMydrive(mydrive);
        dirRoot = new Dir(dirID,"root","rwxdr-x-", parent);
        setDir(dirRoot);
	
    }
    
    @Override
    public String toString() {
        return "Name:" + getName() + "Username:" + getUsername() + "Umask:" + getUmask();
    }
    

	public void remove() {
        Dir home = getDir();
        for (File f: home.getFile()) {
            f.remove();
        }
        setMydrive(null);
        deleteDomainObject();
    }
}
