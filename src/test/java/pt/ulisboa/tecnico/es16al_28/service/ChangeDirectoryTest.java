package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;


public class ChangeDirectoryTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        Dir dir = new Dir(logged, "DirTest");
    }
	

	/*	Change Directory relative	*/
    @Test
    public void successChangedDir() {
        final String dir = "DirTest";	
        Dir insideDir = (Dir) MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir().getFileByName(dir);
	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        // Check if changed Dir to the right one(DirTest)
        assertEquals(currentDir,insideDir);
    }

    @Test
    public void changeToItself() {
        final String dir = ".";	
        Dir rootDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();
	
        // Change to Itself(root)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        // Check changed Dir to himself(root)
        assertEquals(currentDir,rootDir);
    }

    @Test
    public void changeToParent() {
        final String dir = "DirTest";	
        final String parent = "..";
        Dir rootDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();
	
        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, dir);
        changeService.execute();
	
        //Change to parent directory (root)
        ChangeDirectoryService change2Service = new ChangeDirectoryService(_token, parent);
        change2Service.execute();
	
        Dir parentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

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
	
	/*		Absolute path		*/
    @Test
    public void successChangedDirAbsolute() {
        final String dir = "DirTest";
	final String trashi = "Lixo";	
	MyDrive mydrive = MyDrive.getInstance();
	Login logged = mydrive.getLoginByToken(_token);
	
        User user = new User("Alberto", "1234", "Alberto", "rwxdrwxd", logged);
        Login login = new Login("Alberto", "1234");
	//Cria Pasta Lixo dentro da pasta do Alberto
	Dir trash = new Dir(logged, "Lixo");
	//Vai para a pasta do Alberto        
	logged.cd(mydrive, "..");
	
	//Pasta do Lixo
        Dir insideDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

	//Vai para a pasta do / ou home       
	logged.cd(mydrive, "..");

        // Change to inside directory(DirTest)
        ChangeDirectoryService changeService = new ChangeDirectoryService(_token, "/home/Alberto/Lixo");
        changeService.execute();

	Dir currentDir = MyDriveService.getMyDrive().getLoginByToken(_token).getCurrentDir();

        // Check if changed Dir to the right one(DirTest)
        assertEquals(currentDir,insideDir);
    }

}  
