package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.io.UnsupportedEncodingException;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;

public class Link extends Link_Base {
    
    /**
     *  Link's constructor
     *  @param  login       Current MyDrive login
     *  @param  name        Link's name
     *  @param  app         Link's content
     */
    public Link(Login login, String name, String app) {
        super();
        init(login.getMydriveL(), name, login.getUser().getUmask(), login.getUser(), login.getCurrentDir());
        setApp(app);
    }
    
    /**
     *  Link's constructor for xml
     *  @param  mydrive      Current MyDrive 
     *  @param  owner        Link's owner
     *  @param  dir          Link's dir
     */
    public Link(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {
     	initxml(mydrive, owner, dir,  xml); 	
    }
    
    
    public void xmlImport(Element linkElement) throws ImportDocumentException {

		super.xmlImport(linkElement);

    }
    
   
    public Element xmlExport() {
        Element element = super.xmlExport();
	    element.setName("Link");
        return element; 
    }
    
}

