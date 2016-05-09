package pt.ulisboa.tecnico.es16al_28.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.service.AbstractServiceTest;
import pt.ulisboa.tecnico.es16al_28.service.CreateFileService;
import pt.ulisboa.tecnico.es16al_28.presentation.*;

public class SystemTest extends AbstractServiceTest {

    private MyDriveShell sh;

    protected void populate() {
        sh = new MyDriveShell();
    }

    @Test
    public void success() {
        new Login(sh).execute(new String[] { "root", "rootroot" } );
        new ChangeWorkingDirectory(sh).execute(new String[] { } );
        new Environment(sh).execute(new String[] { } );
        new Execute(sh).execute(new String[] { "/home/root" } );
        new List(sh).execute(new String[] { } );
        new CreateFileService(sh.getToken(), "Test", "PlainFile").execute();
        new Write(sh).execute(new String[] { "Test", "Texto" } );
        new Key(sh).execute(new String[] { } );
    }
}
