package pt.ulisboa.tecnico.es16al_28.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.Login;

import pt.ulisboa.tecnico.es16al_28.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.IncorrectPasswordException;


public class LoginTest extends AbstractServiceTest {

    protected void populate() {

    }

    @Test
    public void success() {
        final String username = "root";
        LoginService service = new LoginService(username, "rootroot");
        service.execute();
        long token = service.result();
        Login logged = MyDrive.getInstance().getLoginByToken(token);
        boolean result = (token == logged.getToken());

        assertTrue("Login failed", result);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void loginWithInexistentUser() {
        MyDrive mydrive = MyDrive.getInstance();
        LoginService service = new LoginService("Alberto", "1234");
        service.execute();
    }

    @Test(expected = IncorrectPasswordException.class)
    public void loginWithWrongPassword() {
        MyDrive mydrive = MyDrive.getInstance();
        LoginService service = new LoginService("root", "1234");
        service.execute();
    }

}