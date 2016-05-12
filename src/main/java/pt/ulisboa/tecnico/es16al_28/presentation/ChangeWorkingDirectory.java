package pt.ulisboa.tecnico.es16al_28.presentation;

import pt.ulisboa.tecnico.es16al_28.service.ChangeDirectoryService;

public class ChangeWorkingDirectory extends MyDriveCommand {

    public ChangeWorkingDirectory(MyDriveShell sh) {
        super(sh, "cwd", "Change working directory");
    }

    public void execute(String args[]) {
    	MyDriveShell sh = (MyDriveShell) shell();
    	long token = sh.getToken();
    	ChangeDirectoryService change;
		if (args.length == 0) {
            change = new ChangeDirectoryService(token, "/");
        }
        else {
            change = new ChangeDirectoryService(token , args[0]);
        }
        change.execute();
    }
    
}
