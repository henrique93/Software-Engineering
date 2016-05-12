package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import org.jdom2.Document;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.DomainRoot;

import java.lang.Integer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.PrintStream;

import pt.ulisboa.tecnico.es16al_28.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;


public class MyDrive extends MyDrive_Base {
	static final Logger log = LogManager.getRootLogger();

	public static MyDrive getInstance() {
        MyDrive app = FenixFramework.getDomainRoot().getMydrive();
        if (app != null) {
            return app;
        }
        else {
            log.trace("new Drive");
            return new MyDrive();
        }
    }
    
    /**
     *  MyDrive constructor
     */
    public MyDrive() {
        super.setRoot(FenixFramework.getDomainRoot());
        super.setId(0);
        Dir slash = new Dir (this);
        Dir home = new Dir (this, slash);
        super.setRootDir(home);
        SuperUser root = new SuperUser(this);
        super.setSuperUser(root);
        slash.setOwner(root);
        home.setOwner(root);
        Guest guest = new Guest(this);
        super.setGuest(guest);
    }

    /**
     *  Getter for user by username
     *  @param  username    Username from the User we wish to find
     *  @return user        User correspondant to the given username
     */
    public User getUserByUsername(String username) throws UserDoesNotExistException {
        for (User u: getUserSet()) {
            if (u.getUsername() == username) {
                return u;
            }
        }
        throw new UserDoesNotExistException(username);
    }

    /**
     *  Getter for login by token
     *  @param  token       Token from the Lofin we wish to find
     *  @return login       Login correspondant to the given token
     */
    public Login getLoginByToken(Long token) throws TokenDoesNotExistException {
        if(!getLogedSet().isEmpty()) {
            for (Login login : getLogedSet()) {
                if (login.getToken().equals(token)) {
                    if(login.CheckValidity()){
                        return login;
                    }
                }
            }
        }
        throw new TokenDoesNotExistException();
    }

    /**
     *  Getter for login by token
     *  @param  token       Token from the Lofin we wish to find
     *  @return login       Login correspondant to the given token
     */
    public boolean isLogged(Long token) {
        if(!getLogedSet().isEmpty()) {
            for (Login login : getLogedSet()) {
                Long loggedToken = login.getToken();
                if (loggedToken.equals(token)) {
                    if (login.CheckValidity()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *  ID incrementor
     *  @return id          Current ID (essentialy the number of files created in this MyDrive application)
     */
    public int incID() {
        int id = getId();
        id++;
        super.setId(id);
        return id;
    }
    

    /**
     *  XML Import
     */
    public void xmlImport(Element element) throws ImportDocumentException {
        for (Element node: element.getChildren("user")) {
            String username = node.getAttribute("username").getValue();
            User user = getUserByUsername(username);
            if (user == null) {
                user = new User(this, node);
            }
            user.xmlImport(node);
        }
    } 

    /**
     *  XML Export
     */
    public Document xmlExport() {
        Element element = new Element("MyDrive");
        Document doc = new Document(element);
        element.setAttribute("id", Integer.toString(getId()));
        for (User u: getUserSet()) {
            element.addContent(u.xmlExport());
        }
        return doc;
    }



    /* OVERRIDE GETTERS AND SETTERS FOR SECURITY */
    @Override
    public void setRoot(DomainRoot root) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setId(Integer id) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setRootDir(Dir dir) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setSuperUser(SuperUser superUser) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

    @Override
    public void setGuest(Guest guest) throws PermissionDeniedException {
        throw new PermissionDeniedException();
    }

}
