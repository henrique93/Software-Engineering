package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.*;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.presentation.Hello;

import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.CyclicLinkException;

@RunWith(JMockit.class)
public class ExecuteFileTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
		Login logged = new Login("root", "rootroot");
		_token = logged.getToken();
        new App(logged , "test", "pt.ulisboa.tecnico.es16al_28.presentation.Hello");
        new Link(logged, "CyclicLink1", "/home/root/CyclicLink2");
        new Link(logged, "CyclicLink2", "/home/root/CyclicLink1");
    }

    @Test
    public void success(@Mocked final ExecuteFileService service) {
    	String appName = "executavel";
		String args[] = { };
        MyDrive mydrive = MyDrive.getInstance();
    	Login l = mydrive.getLoginByToken(_token);
    	Dir currentDir = l.getCurrentDir();
    	
    	App app = new App(l , appName, "pt.ulisboa.tecnico.es16al_28.presentation.Hello");
    	ExecuteFileService executeService = new ExecuteFileService(_token, appName, args);
        executeService.execute();
        new Verifications() {{
            service.execute();
        }};
    }

    @Test
    public void testExecutePlainFile(@Mocked final ExecuteFileService service) {
        String fileName = "executavel";
        String content = "pt.ulisboa.tecnico.es16al_28.presentation.Hello \n pt.ulisboa.tecnico.es16al_28.presentation.Hello.bye";
        String args[] = { };
        MyDrive mydrive = MyDrive.getInstance();
        Login l = mydrive.getLoginByToken(_token);
        Dir currentDir = l.getCurrentDir();

        PlainFile plainFile = new PlainFile(l , fileName, content);
        ExecuteFileService executeService = new ExecuteFileService(_token, fileName, args);
        executeService.execute();
        new Verifications() {{
            service.execute();
        }};
	}

    @Test
    public void testExecuteLink(@Mocked final ExecuteFileService service) {
        String linkName = "executavel";
        String content = "/home/root/test";
        String args[] = { };
        MyDrive mydrive = MyDrive.getInstance();
        Login l = mydrive.getLoginByToken(_token);
        Dir currentDir = l.getCurrentDir();

        PlainFile plainFile = new PlainFile(l , linkName, content);
        ExecuteFileService executeService = new ExecuteFileService(_token, linkName, args);
        executeService.execute();
        new Verifications() {{
            service.execute();
        }};
    }

    @Test(expected = PermissionDeniedException.class)
    public void ExecuteWithoutPermission() {
        String appName = "executavel";
        String args[] = { };
        MyDrive mydrive = MyDrive.getInstance();
        Login l = mydrive.getLoginByToken(_token);
        User u1 = new User("pedro", "12345678", "pedro", "rwxd----",l);
        l = new Login("pedro","12345678");
        App app = new App(l , appName, "pt.ulisboa.tecnico.es16al_28.presentation.Hello");
        ExecuteFileService executeService = new ExecuteFileService(l.getToken(), appName, args);
        executeService.execute();
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void ExecuteWithInexistentFile() {
        String appName = "executavel";
        String args[] = { };
        MyDrive mydrive = MyDrive.getInstance();
        Login l = mydrive.getLoginByToken(_token);
        ExecuteFileService executeService = new ExecuteFileService(_token, appName, args);
        executeService.execute();
    }

    @Test(expected = CyclicLinkException.class)
    public void executeCyclicLink() {
        final String name = "CyclicLink1";
        String args[] = { };
        ExecuteFileService service = new ExecuteFileService(_token, name, args);
        service.execute();
    }
}
