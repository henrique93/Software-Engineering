package pt.ulisboa.tecnico.es16al_28.service;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import mockit.*;


import  pt.ulisboa.tecnico.es16al_28.domain.*;

import pt.ulisboa.tecnico.es16al_28.exception.NotFileException;
import pt.ulisboa.tecnico.es16al_28.exception.NotDirException;
import pt.ulisboa.tecnico.es16al_28.exception.NoSuchFileOrDirectoryException;
import pt.ulisboa.tecnico.es16al_28.exception.InvalidContentException;

@RunWith(JMockit.class)
public class EnvironmentLinksTest extends AbstractServiceTest {
    
    private long _token;
    private String _path;
    private Link linky;
    private App apper;
    private static final String path = "/home/$User/App";
    private static final String fPath = "/home/root/App"; 

    protected void populate() {	
	Login logged = new Login("root", "rootroot");
        _token = logged.getToken();
        Dir folder = new Dir(logged, "Directory");
	apper = new App(logged, "App" , "Heyheyhey");
	linky = new Link(logged, "Link", path);
	EnvironmentVariable ev = new EnvironmentVariable(logged ,"User","root");
	logged.addEnvVar(ev);
    }

    @Test
    public void success() {
	MyDrive mydrive = MyDrive.getInstance();
	Login logged = mydrive.getLoginByToken(_token);
	
	Link linker = new Link(logged, "App" , fPath);

	new Expectations() {
		{
		linky = linker;			
		}
	};
	
        assertEquals(apper.readFile(logged),linky.readFile(logged));
    }

    @Test(expected = InvalidContentException.class)
    public void findNonDir() throws InvalidContentException {
	
	final String nome = "Link";
	MyDrive mydrive = MyDrive.getInstance();
	Login logged = mydrive.getLoginByToken(_token);
        
	new MockUp<Link>(){
		@Mock
	   	String readFile(Login l)  throws InvalidContentException {
			throw new InvalidContentException("Directory");
		}
		
	};

	Link linkest = new Link (logged, "meh meh","/home/root/Directory");
       	linkest.readFile(logged);
    }

 }
