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
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.NotDirException;
import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;


public class ChangeDirectoryTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        Dir dir = new Dir(logged, "DirTest");
        Link link = new Link(logged, "Linky","Banana");
    }
	

	/*	Change Directory relative	*/
    @Test
    public void successChangedDir() {
        final String dir = "DirTest";
	MyDrive drivy=MyDriveService.getMyDrive();
	Login log = drivy.getLoginByToken(_token);
	Dir insideDir = log.getCurrentDir();
        insideDir = (Dir) insideDir.getFileByName(dir);
	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        Dir currentDir = log.getCurrentDir();

        // Check if changed Dir to the right one(DirTest)
        assertEquals(currentDir,insideDir);
    }

    @Test
    public void changeToItself() {
        final String dir = ".";	
	MyDrive drivy=MyDriveService.getMyDrive();
	Login log = drivy.getLoginByToken(_token);
        Dir rootDir = log.getCurrentDir();
	
        // Change to Itself(root)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        Dir currentDir = log.getCurrentDir();

        // Check changed Dir to himself(root)
        assertEquals(currentDir,rootDir);
    }

    @Test
    public void changeToParent() {
        final String dir = "DirTest";	
        final String parent = "..";
	MyDrive drivy=MyDriveService.getMyDrive();
	Login log = drivy.getLoginByToken(_token);
        Dir rootDir = log.getCurrentDir();
	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        //Change to parent directory (root)
        ChangeDirectoryService change2Service = new ChangeDirectoryService(_token, parent);
        change2Service.execute();
	
        Dir parentDir = log.getCurrentDir();

        // Check changed Dir to the right one
        assertEquals(parentDir,rootDir);
    }

    @Test
    public void rootChangeToParent() {
        final String dir = "..";	
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
    }

    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void changeToNonexistentDir() {
        final String dir = "NonexistentDir";
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
    }

    @Test(expected = NotDirException.class)
    public void notADir() {
        final String dir = "Linky";
	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
    }

    @Test(expected = PermissionDeniedException.class)
    public void noPermissionAbsolute() {
        final String dir = "..";	
        final String noPerm = "Syke";

        MyDrive mydrive = MyDrive.getInstance();
        Login logged = mydrive.getLoginByToken(_token);
	
        User user = new User("Alberto", "12345678", "Alberto", "rwxd----", logged);
        Login login = new Login("Alberto", "12345678");
        long token = login.getToken();

        //Create Dir Syke inside Alberto's Directory
        Dir Trash = new Dir(login, "Syke");

        user = new User("Peter", "12345678", "Peter", "rwxd----", logged);
        login = new Login("Peter", "12345678");
        token = login.getToken();

        //Go to /home
        ChangeDirectoryService changeService = new ChangeDirectoryService(token, dir);
        changeService.execute();

        // To Alberto's Folder although Peter doesn't have permission
        ChangeDirectoryService changeService2 = new ChangeDirectoryService(token, noPerm);

    }
	
	/*		Absolute path		*/
    @Test
    public void successChangedDirAbsolute() {
        final String dir = "DirTest";
        final String trash = "Lixo";
        MyDrive mydrive = MyDrive.getInstance();
        Login logged = mydrive.getLoginByToken(_token);
	
        User user = new User("Alberto", "12345678", "Alberto", "rwxdrwxd", logged);
        Login login = new Login("Alberto", "12345678");
        long token = login.getToken();

        //Create Directory Lixo inside Alberto's Directory
        Dir Trash = new Dir(login, "Lixo");
	logged = mydrive.getLoginByToken(token);
        //Get directory Lixo
        Dir insideDir = logged.getCurrentDir();
	insideDir = (Dir) insideDir.getFileByName(trash);
        //Go to directory root
        login.cd("..");

        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(token, "/home/Alberto/Lixo");
        changeService.execute();

        Dir currentDir = logged.getCurrentDir();

        // Check if changed Dir to the right one(DirTest)
        assertEquals(currentDir,insideDir);
    }

    @Test
    public void changedToRootAbsolute() {	
        MyDrive mydrive = MyDrive.getInstance();
        Login logged = mydrive.getLoginByToken(_token);

        logged.cd("..");

	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, "/");
        changeService.execute();

        Dir currentDir = logged.getCurrentDir();
	Dir rootDir = mydrive.getRootDir();
        // Check if changed Dir to the right one(DirTest)
        assertEquals(currentDir, rootDir.getParent());
    }
    
    @Test(expected = NoSuchFileOrDirectoryException.class)
    public void NoSuchDirAbsolute() {
		MyDrive mydrive = MyDrive.getInstance();
		Login logged = mydrive.getLoginByToken(_token);


        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, "/home/root/Atum");
        changeService.execute();
    }

    @Test(expected = NotDirException.class)
    public void notADirAbsolute() {
        final String dir = "DirTest";
        final String trash = "Linker";
        MyDrive mydrive = MyDrive.getInstance();
        Login logged = mydrive.getLoginByToken(_token);
	
        User user = new User("Alberto", "12345678", "Alberto", "rwxdrwxd", logged);
        Login login = new Login("Alberto", "12345678");
        long token = login.getToken();

        //Create Link Linker inside Alberto's Directory
        Link Trash = new Link(login, "Linker","Banana");

        //Go to directory root
        login.cd("..");

        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(token, "/home/Alberto/Linker");
        changeService.execute();
    }


}  
