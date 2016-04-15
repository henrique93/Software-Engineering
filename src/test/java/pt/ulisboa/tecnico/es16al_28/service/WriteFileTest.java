package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class WriteFileTest extends AbstractServiceTest {
	
    private long _token;

    protected void populate() {
        MyDrive md = MyDrive.getInstance();
        Login l = new Login("root", "rootroot");
        _token = l.getToken();
        User u = new User("1user", "abcde", "1user", "rwxdrwx-",l);
        l = new Login("1user", "abcde");
        PlainFile f = new PlainFile(l, "ficheiro1", "isto existe inicialmente 1");
        l = md.getLoginByToken(_token);
        u = new User("2user", "qwerty", "2user", "rwxdrwx-", l);
        l = new Login("2user", "qwerty");
        f = new PlainFile(l, "ficheiro2", "isto existe inicialmente 2");
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
       User u1 = new User("pedro", "qaz", "pedro", "rwxdrwx-",l);
       l = new Login("pedro","qaz");
       l.cd("..");
       l.cd("root");
       long token = l.getToken();
       WriteFileService service = new WriteFileService(token, file, content);
       service.execute();
   }


}
