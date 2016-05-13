package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Link;

import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.CyclicLinkException;

public class ReadFileTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        PlainFile pf = new PlainFile(logged, "Alberto?", "ou Jacinto?");
        Dir dir = new Dir(logged, "TestDir");
        Link link1 = new Link(logged, "CyclicLink1", "/home/root/CyclicLink2");
        Link link2 = new Link(logged, "CyclicLink2", "/home/root/CyclicLink1");
    }

    @Test
    public void success() {
        final String name = "Alberto?";
        ReadFileService service = new ReadFileService(_token, name);
        service.execute();
        String result = service.result();
        assertEquals("ou Jacinto?", result);
    }


    @Test
    public void readEmptyFile() {
        final String name = "Jacinto?";
        Login logged = new Login("root", "rootroot");
        PlainFile pf = new PlainFile(logged, "Jacinto?", "");
        long token = logged.getToken();
        ReadFileService service = new ReadFileService(token, name);
        service.execute();
    }

    @Test(expected = PermissionDeniedException.class)
    public void readFileWithoutPermission() {
        final String name = "Jacinto?";
        Login logged = new Login("root", "rootroot");
        PlainFile pf = new PlainFile(logged, "Jacinto?", "ou Alberto?");
        User user = new User("Alberto", "12345678", "Alberto", "rwxd--x-", logged);
        Login login = new Login("Alberto", "12345678");
        login.cd("..");
        login.cd("root");
        long token = login.getToken();
        ReadFileService service = new ReadFileService(token, name);
        service.execute();
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void readInexistantFile() {
        final String name = "Jacinto?";
        ReadFileService service = new ReadFileService(_token, name);
        service.execute();
    }

    @Test(expected = NotFileException.class)
    public void readDirectory() {
        final String name = "TestDir";
        ReadFileService service = new ReadFileService(_token, name);
        service.execute();
    }

    @Test(expected = CyclicLinkException.class)
    public void readCyclicLink() {
        final String name = "CyclicLink1";
        ReadFileService service = new ReadFileService(_token, name);
        service.execute();
    }

}
