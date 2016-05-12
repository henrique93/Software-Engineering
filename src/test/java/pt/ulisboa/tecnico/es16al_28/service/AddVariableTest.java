package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.service.MyDriveService;
import pt.ulisboa.tecnico.es16al_28.service.dto.EnvironmentVariableDto;
import pt.ulisboa.tecnico.es16al_28.domain.EnvironmentVariable;


public class AddVariableTest extends AbstractServiceTest {

    private long _token;
    
    protected void populate() {
        Login l = new Login("root", "rootroot");
        _token = l.getToken();
        EnvironmentVariable ev = new EnvironmentVariable(l,"$ABC", "/home/root");
    }

    @Test
    public void success() {
        String name = "$ABC";
        String value = "/home/root";
        Login l = MyDrive.getInstance().getLoginByToken(_token);
        EnvironmentVariable env = new EnvironmentVariable(l, name, value);
        
        
        AddVariableService service = new  AddVariableService(_token, name, value);
        service.execute();
        List<EnvironmentVariableDto> ps = service.result();
        
        assertEquals(env.getName(), ps.get(0).getName());
        assertEquals(env.getValue(), ps.get(0).getValue());
    }


}
