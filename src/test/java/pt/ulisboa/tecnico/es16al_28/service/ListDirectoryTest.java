package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.*;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.User;
import pt.ulisboa.tecnico.es16al_28.domain.Link;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.service.dto.FileDto;

import pt.ulisboa.tecnico.es16al_28.exception.PermissionDeniedException;

public class ListDirectoryTest extends AbstractServiceTest {

    private long _token;

    protected void populate() {
        Login l = new Login("root", "rootroot");
        _token = l.getToken();
    }

    @Test
    public void success() {
        Login l = MyDrive.getInstance().getLoginByToken(_token);
        Dir dir1 = new Dir(l, "firstDir");
        Dir dir2 = new Dir(l, "secondDir");
        Link link = new Link(l, "link", "linkTest");
        App app = new App(l, "app", "appTest");
        PlainFile plainfile = new PlainFile(l, "plainfile", "plainfileTest");

        ListDirectoryService service = new ListDirectoryService(_token);
        service.execute();
        List<FileDto> ps = service.result();
        
        // Check list
        assertEquals(l.getCurrentDir().getName(), ps.get(0).getName());
        assertEquals(l.getCurrentDir().getParent().getPermission(), ps.get(1).getPermission());
        assertEquals(dir1.getName(), ps.get(2).getName());
        assertEquals(dir2.getId(), ps.get(3).getId());
        assertEquals(link.getPermission(), ps.get(4).getPermission());
        assertEquals(app.getLastChange(), ps.get(5).getLastChange());
        assertEquals(plainfile.getOwner().getUsername(), ps.get(6).getOwner());
    }

    @Test
    public void successEmptyDirectory() {
        Login l = MyDrive.getInstance().getLoginByToken(_token);
        ListDirectoryService service = new ListDirectoryService(_token);
        service.execute();
        List<FileDto> ps = service.result();

        // Check list
        assertEquals(l.getCurrentDir().getName(), ps.get(0).getName());
        assertEquals(l.getCurrentDir().getParent().getLastChange(), ps.get(1).getLastChange());
    }

    @Test(expected = PermissionDeniedException.class)
    public void ListFileWithoutPermission() {
        MyDrive mydrive = MyDriveService.getMyDrive();
        Login logged = mydrive.getLoginByToken(_token);
        User user = new User("Jacinto", "987654321", "Alfredo", "rwxd--x-", logged);
        Login login = new Login("Jacinto", "987654321");
        login.cd("..");
        login.cd("root");
        long token = login.getToken();
        ListDirectoryService service = new ListDirectoryService(token);
        service.execute();
    } 

}
