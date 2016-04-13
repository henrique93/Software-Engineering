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

}