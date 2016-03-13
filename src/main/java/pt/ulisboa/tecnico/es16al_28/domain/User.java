package pt.ulisboa.tecnico.es16al_28.domain;

public class User extends User_Base {
    
    public User(String username, String password, String name, String umask) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
    }

    /**
     *  Super User special constructor
     */
    public User() {
        setUsername("root");
        setPassword("rootroot");
        setName("Super User");
        setUmask("rwxdr-x-");
    }
    
    @Override
    public String toString() {
        return "Name:" + getName() + "Username:" + getUsername() + "Umask:" + getUmask();
    }
    
}
