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
        List<String> ps = service.result();
        
        // Check list
        assertEquals(l.getCurrentDir().toString(), ps.get(0));
        assertEquals(l.getCurrentDir().getParent().toString(), ps.get(1));
        assertEquals(dir1.toString(), ps.get(2));
        assertEquals(dir2.toString(), ps.get(3));
        assertEquals(link.toString(), ps.get(4));
        assertEquals(app.toString(), ps.get(5));
        assertEquals(plainfile.toString(), ps.get(6));
    }

    @Test
    public void successEmptyDirectory() {
        Login l = MyDrive.getInstance().getLoginByToken(_token);
        ListDirectoryService service = new ListDirectoryService(_token);
        service.execute();
        List<String> ps = service.result();

        // Check list
        assertEquals(l.getCurrentDir().toString(), ps.get(0));
        assertEquals(l.getCurrentDir().getParent().toString(), ps.get(1));
    }

}
