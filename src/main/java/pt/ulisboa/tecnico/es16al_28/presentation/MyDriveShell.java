package pt.ulisboa.tecnico.es16al_28.presentation;

import java.util.Collections;
import java.util.HashMap;

import pt.ulisboa.tecnico.es16al_28.service.dto.EnvironmentVariableDto;

public class MyDriveShell extends Shell {

    private long _token;
    private String _username;
    private HashMap<String, String> EnvVarList = new HashMap<String, String>();
    private HashMap<String, Long> LoginList = new HashMap<String, Long>();
    private HashMap<Long, HashMap> VarsLogin = new HashMap<Long, HashMap>();

    public static void main(String[] args) throws Exception {
        MyDriveShell sh = new MyDriveShell();
        sh.execute();
    }

    public MyDriveShell() {
        super("MyDrive");
        new Login(this);
        new ChangeWorkingDirectory(this);
        new Environment(this);
        new Execute(this);
        new Key(this);
        new List(this);
        new Write(this);
    }


    /* GETTERS AND SETTERS token */
    public void setUsername(String username) {
        _username = username;
    }

    public String getUsername() {
        return _username;
    }


    /* GETTERS AND SETTERS token */
    public void setToken(long token) {
        _token = token;
    }

    public long getToken() {
        return _token;
    }


    /* GETTERS AND SETTERS LoginList */
    public void setLogin(String username, long token) {
        LoginList.put(username, token);
    }

    public long getUserToken(String username) {
        return LoginList.get(username);
    }


    /* GETTERS AND SETTERS EnvVarList */
    public void setEnvVar(String name, String value) {
        EnvVarList.put(name, value);
    }

    public String getEvValue(String EvName) {
        return EnvVarList.get(EvName);
    }

    public HashMap<String, String> getEnvVarList() {
        return EnvVarList;
    }

    public void changeEnvVarList(HashMap<String, String> evList) {
        EnvVarList = evList;
    }


    /* GETTERS AND SETTERS VarsLogin */
    public void setVarsLogin(HashMap<String, String> evList) {
        VarsLogin.put(_token, evList);
    }

    public HashMap getVarsLogin(long token) {
        return VarsLogin.get(token);
    }

    public boolean loginHasEVs(long token) {
        return VarsLogin.containsKey(token);
    }

}