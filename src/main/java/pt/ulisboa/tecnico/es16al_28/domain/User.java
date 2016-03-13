package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;

/* Import exceptions */
import java.io.UnsupportedEncodingException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;

public class User extends User_Base {
    
    public User(String username, String password, String name, String umask) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
    }

   public User(Element xml) throws ImportDocumentException, NotFileException {
        xmlImport(xml);
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
	

     public void xmlImport(Element userElement) throws ImportDocumentException, NotFileException {

        try {
            setUsername(new String(userElement.getAttribute("username").getValue().getBytes("UTF-8")));
            setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
            setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
            setUmask(new String(userElement.getAttribute("umask").getValue().getBytes("UTF-8")));
            
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
       
    }    

}
