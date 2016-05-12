package pt.ulisboa.tecnico.es16al_28.presentation;

public class Key extends MyDriveCommand {

    public Key(MyDriveShell sh) {
        super(sh, "token", "Change session");
    }

    public void execute(String args[]) {
   		MyDriveShell sh = (MyDriveShell) shell();
    	long token;
		if (args.length == 0) {
			token = sh.getToken();
			String username = sh.getUsername();
            System.out.println("token: " + token);
            System.out.println("username: " + username);
        }
        else {
			token = sh.getUserToken(args[0]);
			sh.setUsername(args[0]);
			sh.setToken(token);
			System.out.println("token: " + token);
        }
    }

}
