package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.User;

import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.CyclicLinkException;

public class WriteFileTest extends AbstractServiceTest {

	private long _token;

    protected void populate() {
        MyDrive md = MyDrive.getInstance();
        Login l = new Login("root", "rootroot");
        _token = l.getToken();
        User u = new User("1user", "12345678", "1user", "rwxdrwx-",l);
        l = new Login("1user", "12345678");
        PlainFile f = new PlainFile(l, "ficheiro1", "isto existe inicialmente 1");
        l = md.getLoginByToken(_token);
        u = new User("2user", "12345678", "2user", "rwxdrwx-", l);
        l = new Login("2user", "12345678");
        f = new PlainFile(l, "ficheiro2", "isto existe inicialmente 2");
        new Dir(l, "TestDir");
        new Link(l, "CyclicLink1", "/home/2user/CyclicLink2");
        new Link(l, "CyclicLink2", "/home/2user/CyclicLink1");
        _token = l.getToken();
    }

    @Test
    public void success() {
    	final String content = "agora é isto 2";
    	MyDrive md = MyDrive.getInstance();
    	Login l = md.getLoginByToken(_token);
    	PlainFile f =(PlainFile) l.getCurrentDir().getFileByName("ficheiro2");
        WriteFileService service = new WriteFileService(_token, "ficheiro2", content);
        service.execute();
        assertEquals(content, f.readFile(l));
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void writeNonExistingFile() {
    	 final String content = "vai dar erro";
         final String file = "DoesNotExist";
        WriteFileService service = new WriteFileService(_token, file, content);
        service.execute();
    }
   
    @Test(expected = PermissionDeniedException.class)
    public void writeFileWithoutPermission() {
        final String file = "aaa";
        final String content = "vai dar erro";
        MyDrive md = MyDrive.getInstance();
        Login l = new Login("root", "rootroot");
        PlainFile f = new PlainFile(l, file, "blabla");
        User u1 = new User("pedro", "12345678", "pedro", "rwxd----",l);
        l = new Login("pedro","12345678");
        l.cd("..");
        l.cd("root");
        long token = l.getToken();
        WriteFileService service = new WriteFileService(token, file, content);
        service.execute();
	
    }

    @Test(expected = NotFileException.class)
    public void writeDirectory() {
        final String name = "TestDir";
        ReadFileService service = new ReadFileService(_token, name);
        service.execute();
    }

    @Test(expected = CyclicLinkException.class)
    public void writeCyclicLink() {
        final String name = "CyclicLink1";
        final String content = "Can not write";
        WriteFileService service = new WriteFileService(_token, name, content);
        service.execute();
    }

}
