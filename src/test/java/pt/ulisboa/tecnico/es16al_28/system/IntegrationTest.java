package pt.ulisboa.tecnico.es16al_28.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive; // Mockup
import pt.ulisboa.tecnico.es16al_28.service.*;
import pt.ulisboa.tecnico.es16al_28.service.dto.*;
import pt.ulisboa.tecnico.es16al_28.exception.*;


@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private static final List<String> names = new ArrayList<String>();
    private long _token;
    private static final String p1 = "Tiago", p2 = "Miguel", p3 = "Xana";
    private static final String p4 = "António", p5 = "Ana";
    private static final String d1 = "TestDir", pf1 = "TestPlainFile";
    private static final String a1 = "TestApp", l1 = "TestLink";
    private static final String appContent = "pt.ulisboa.tecnico.es16al_28.presentation.Hello";
    private static final String password = "12345678";
    private static final String importFile = "other.xml";
    private String args[] = { };

    protected void populate() { // populate mockup
        names.add(p2);
        names.add(p4);
    }

    @Test
    public void success() throws Exception {
        MyDrive mydrive = MyDrive.getInstance();
        LoginService login = new LoginService("root", "rootroot");
        login.execute();
        _token = login.result();

        new CreateFileService(_token, d1, "Dir").execute();

        ListDirectoryService listService = new ListDirectoryService(_token);
        listService.execute();
        for (FileDto dto: listService.result()) {
            System.out.println(dto.toString());
        }

        AddVariableService evService = new AddVariableService(_token, "NewEV", "1234");
        evService.execute();
        for (EnvironmentVariableDto dto: evService.result()) {
            System.out.println(dto.toString());
        }

        new ChangeDirectoryService(_token, d1).execute();

        new CreateFileService(_token, pf1, "PlainFile").execute();

        new DeleteFileService(_token, pf1).execute();

        new CreateFileService(_token, a1, "App", appContent).execute();

        new CreateFileService (_token, pf1, "PlainFile").execute();

        new WriteFileService(_token, pf1, "Hello professor, this actually works");

        new ExecuteFileService(_token, a1, args);

        ReadFileService readService = new ReadFileService(_token, pf1);
        readService.execute();
        System.out.println(readService.result());

    }
}