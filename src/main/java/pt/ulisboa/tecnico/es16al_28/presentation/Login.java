package pt.ulisboa.tecnico.es16al_28.presentation;

import pt.ulisboa.tecnico.es16al_28.service.LoginService;

public class Login extends MyDriveCommand {

    public Login(MyDriveShell sh) {
        super(sh, "login", "Create new session");
    }

    public void execute(String args[]) {
        if (args.length < 1) {
            throw new RuntimeException("USAGE: "+name()+" <username> [<password>]");
        }
        LoginService login;
        if (args.length == 1) {
            login = new LoginService(args[0], null);
        }
        else {
            login = new LoginService(args[0], args[1]);
        }
        login.execute();
        long token = login.result();
        MyDriveShell sh = (MyDriveShell) shell();
        Long lastUser = sh.getToken();
        if(lastUser != null && sh.loginHasEVs(lastUser)) {
            sh.setVarsLogin(sh.getEnvVarList());
        }
        if (sh.loginHasEVs(lastUser)) {
            sh.changeEnvVarList(sh.getVarsLogin(lastUser));
        }
        sh.setToken(token);
        sh.setLogin(args[0], token);
    }
    
}
