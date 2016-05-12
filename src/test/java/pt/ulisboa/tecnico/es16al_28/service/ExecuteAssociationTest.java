package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import mockit.*;

import  pt.ulisboa.tecnico.es16al_28.domain.*;

import pt.ulisboa.tecnico.es16al_28.exception.InvalidExtensionException;

			//one test missing: file without extension

@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest {

    private long _token;



    protected void populate() {
	Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
	


    }



    @Test
    public void success() {
      	MyDrive mydrive = MyDrive.getInstance();
	Login logged = mydrive.getLoginByToken(_token);
	String args[] = { };
	PlainFile pf = new PlainFile(logged, "test", "stuff");
	App app = new App(logged, "runit", "pt.ulisboa.tecnico.es16al_28.presentation.Hello");
	Association extension = new Association();
	extension.setName(".pdf");
	extension.setAppA(app);
	pf.setExtensionF(extension);
	ExecuteFileService service = new ExecuteFileService(_token, "test",args);
	new MockUp<ExecuteFileService>(){		
		@Mock	    
		void dispatch() {}
				
		};

	ExecuteFileService executeService = new ExecuteFileService (_token, "test", args);
	executeService.execute();
	
        new Verifications() {{
            service.execute();
        }};
    }	



    

		
    @Test(expected = InvalidExtensionException.class)
    public void InvalidExtensionTest() {
	MyDrive mydrive = MyDrive.getInstance();
	Login logged = mydrive.getLoginByToken(_token);
 	String args[] = { };
	PlainFile pf = new PlainFile(logged, "test", "stuff");
	App app = new App(logged, "runit", "pt.ulisboa.tecnico.es16al_28.presentation.Hello");
	Association extension = new Association();
	extension.setName(".sfd");
	extension.setAppA(app);
	pf.setExtensionF(extension);

	new MockUp<ExecuteFileService>(){
		@Mock
	   	void dispatch()  throws InvalidExtensionException {
			throw new InvalidExtensionException(app.getName(), extension.getName());
			}		
		};
	ExecuteFileService executeService = new ExecuteFileService (_token, "test", args);
	executeService.execute();

	
    	}


}
