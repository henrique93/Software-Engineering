package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;

public class ReadFileTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        PlainFile pf = new PlainFile(logged, "Alberto?", "ou Jacinto?");
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
        User user = new User("Alberto", "1234", "Alberto", "rwxd--x-", logged);
        Login login = new Login("Alberto", "1234");
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

}
