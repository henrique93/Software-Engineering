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
        DeleteFileService linkService = new DeleteFileService(link, _token);
        linkService.execute();
        DeleteFileService appService = new DeleteFileService(app, _token);
        appService.execute();
        DeleteFileService plainFileService = new DeleteFileService(plainfile, _token);
        plainFileService.execute();
        DeleteFileService dirService = new DeleteFileService(dir, _token);
        dirService.execute();
        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        // Check if files were removed
        assertFalse("File was not removed", currentDir.directoryHasFile(link));
        assertFalse("File was not removed", currentDir.directoryHasFile(app));
        assertFalse("File was not removed", currentDir.directoryHasFile(plainfile));
        assertFalse("File was not removed", currentDir.directoryHasFile(dir));
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void DeleteNonexistingFile() {
        final String file = "DoesNotExist";
        DeleteFileService service = new DeleteFileService(file, _token);
        service.execute();
    }

    @Test(expected = PermissionDeniedException.class)
    public void DeleteFileWithoutPermission() {
        final String file = "LinkTest";
        MyDrive mydrive = MyDriveService.getMyDrive();
        Login logged = mydrive.getLoginByToken(_token);
        User user = new User("Alberto", "1234", "Alberto", "rwxdr-x-", logged);
        Login login = new Login("Alberto", "1234");
        login.cd(mydrive, "..");
        login.cd(mydrive, "root");
        long token = login.getToken();
        DeleteFileService service = new DeleteFileService(file, token);
        service.execute();
    }

}
