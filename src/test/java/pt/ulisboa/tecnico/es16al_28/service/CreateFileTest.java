package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;

import pt.ulisboa.tecnico.es16al_28.exception.FileAlreadyExistsException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotCreateWithContentException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotCreateWithoutContentException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidNameException;
import pt.ulisboa.tecnico.es16al_28.exception.TooLongException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidContentException;

public class CreateFileTest extends AbstractServiceTest {

	long _token;

    protected void populate() {
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
    }   

    @Test
    public void success() {
        final String link = "LinkTest";
        final String app = "AppTest";
        final String plainfile = "PlainFileTest";
        final String dir = "DirTest";
        final String content = "Cool";

        CreateFileService linkService = new CreateFileService(_token, link, "link", content);
        linkService.execute();
        CreateFileService appService = new CreateFileService(_token, app, "app", content);
        appService.execute();
        CreateFileService plainFileService = new CreateFileService(_token, plainfile, "plainfile" , content); 
        plainFileService.execute();
        CreateFileService dirService = new CreateFileService(_token, dir, "dir");
        dirService.execute();

        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();
        
        // Check if files were created
        assertTrue("File was not created", currentDir.directoryHasFile(link));
        assertTrue("File was not created", currentDir.directoryHasFile(app));
        assertTrue("File was not created", currentDir.directoryHasFile(plainfile));
        assertTrue("File was not created", currentDir.directoryHasFile(dir));
    }

    @Test
    public void CreateFileWithoutContent() {
        final String file = "PlainFileTest";
        CreateFileService service = new CreateFileService(_token, file, "plainfile");
        service.execute();

        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        assertTrue("File was not created", currentDir.directoryHasFile(file));
    }

   @Test(expected = FileAlreadyExistsException.class)
    public void CreateAlreadyExistingFile() {
        final String file = "PlainFileTest";
        final String content = "Cool";
        new PlainFile(MyDrive.getInstance().getLoginByToken(_token), file, content);
        CreateFileService service = new CreateFileService(_token, file, "plainfile", content);
        service.execute();
    }

    @Test(expected = CannotCreateWithoutContentException.class)
    public void CreateLinkWithoutContent() {
        final String file = "Link";
        CreateFileService service = new CreateFileService(_token, file, "link");
        service.execute();
    }

    @Test(expected = CannotCreateWithContentException.class)
    public void CreateDirWithContent() {
        final String file = "Dir";
        final String content = "Cool";
        CreateFileService service = new CreateFileService(_token, file, "dir", content);
        service.execute();
    }

    @Test(expected = PermissionDeniedException.class)
    public void CreateFileWithoutPermission() {
        final String file = "Test";
        MyDrive mydrive = MyDriveService.getMyDrive();
        Login logged = mydrive.getLoginByToken(_token);
        User user = new User("Alberto", "12345678", "Alberto", "rwxdr-x-", logged);
        Login login = new Login("Alberto", "12345678");
        login.cd("..");
        login.cd("root");
        CreateFileService service = new CreateFileService(login.getToken(), file, "dir");
        service.execute();
    }

    @Test(expected = InvalidNameException.class)
    public void CreateFileWithInvalidNameSlash() {
        final String file = "a/c";
        CreateFileService service = new CreateFileService(_token, file, "dir");
        service.execute();
    }

    @Test(expected = InvalidNameException.class)
    public void CreateFileWithInvalidNameSlash0() {
        final String file = "a\0n";
        CreateFileService service = new CreateFileService(_token, file, "dir");
        service.execute();
    }

    @Test(expected = TooLongException.class)
    public void CreateFileWithPathTooLong() {
        final String file = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CreateFileService service = new CreateFileService(_token, file, "dir");
        service.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void CreateAppWithInvalidContent() {
        final String file = "Test";
        final String content = "Conteúdo inválido.java";
        CreateFileService service = new CreateFileService(_token, file, "app", content);
        service.execute();
    }

}