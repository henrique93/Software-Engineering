package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;



public class DeleteFileTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        MyDrive mydrive = MyDrive.getInstance();
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        Link link = new Link(logged, "LinkTest", "DELETE.TEST");
        App app = new App(logged, "AppTest", "DELETE.TEST");
        PlainFile plainfile = new PlainFile(logged, "PlainFileTest", "DELETE.TEST");
        Dir dir = new Dir(logged, "DirTest");
    }

    @Test
    public void success() {
        final String link = "LinkTest";
        final String app = "AppTest";
        final String plainfile = "PlainFileTest";
        final String dir = "DirTest";
        DeleteFileService linkService = new DeleteFileService(_token, link);
        linkService.execute();
        DeleteFileService appService = new DeleteFileService(_token, app);
        appService.execute();
        DeleteFileService plainFileService = new DeleteFileService(_token, plainfile);
        plainFileService.execute();
        DeleteFileService dirService = new DeleteFileService(_token, dir);
        dirService.execute();
        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        // Check if files were removed
        assertFalse("File was not removed", currentDir.directoryHasFile(link));
        assertFalse("File was not removed", currentDir.directoryHasFile(app));
        assertFalse("File was not removed", currentDir.directoryHasFile(plainfile));
        assertFalse("File was not removed", currentDir.directoryHasFile(dir));
    }

    @Test
    public void successDeleteDirectoryNotEmpty() {
        final String file = "DirTest";
        MyDrive mydrive = MyDrive.getInstance();
        Login logged = mydrive.getLoginByToken(_token);
        logged.cd("DirTest");
        Link trash = new Link(logged, "LinkTest2", "DELETE.TEST");
        logged.cd("..");
        DeleteFileService service = new DeleteFileService(_token, file);
        service.execute();

        // Check if directory was removed
        assertFalse("File was not removed", logged.getCurrentDir().directoryHasFile(file));
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void DeleteNonexistingFile() {
        final String file = "DoesNotExist";
        DeleteFileService service = new DeleteFileService(_token, file);
        service.execute();
    }

    @Test(expected = PermissionDeniedException.class)
    public void DeleteFileWithoutPermission() {
        final String file = "LinkTest";
        MyDrive mydrive = MyDriveService.getMyDrive();
        Login logged = mydrive.getLoginByToken(_token);
        User user = new User("Alberto", "1234", "Alberto", "rwxdr-x-", logged);
        Login login = new Login("Alberto", "1234");
        login.cd("..");
        login.cd("root");
        long token = login.getToken();
        DeleteFileService service = new DeleteFileService(token, file);
        service.execute();
    }

}
