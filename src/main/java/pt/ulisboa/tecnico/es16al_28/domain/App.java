package pt.ulisboa.tecnico.es16al_28.domain;

import org.jdom2.Element;
import java.io.UnsupportedEncodingException;

import pt.ulisboa.tecnico.es16al_28.exception.ImportDocumentException;

public class App extends App_Base {
    
    /**
     *  Common App constructor
     *  @param  login       Current MyDrive login
     *  @param  name        App's name
     *  @param  app         App's content
     */
    public App(Login login, String name, String app) {
        super();
        init(login.getMydriveL(), name, login.getUser().getUmask(), login.getUser(), login.getCurrentDir());
        setApp(app);
    }
    
      /**
     *  App's constructor for XML
     *  @param  login       Current MyDrive login
     *  @param  owner        App's owner
     *  @param  dir         App's dir
     *  @param  xml	xml element
     */
    public App(MyDrive mydrive, User owner, Dir dir, Element xml) throws ImportDocumentException {

     	initxml(mydrive, owner, dir,  xml);	
    }
    


    public void xmlImport(Element appElement) throws ImportDocumentException {

		super.xmlImport(appElement);

    }
    
   
    public Element xmlExport() {
        Element element = super.xmlExport();
	element.setName("App");
        return element; 
    }
}
