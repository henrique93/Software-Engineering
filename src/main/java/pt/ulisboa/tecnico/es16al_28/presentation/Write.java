package pt.ulisboa.tecnico.es16al_28.presentation;

import pt.ulisboa.tecnico.es16al_28.service.WriteFileService;

public class Write extends MyDriveCommand {

    public Write(MyDriveShell sh) {
        super(sh, "update", "Update file's content");
    }

    public void execute(String args[]) {
	MyDriveShell sh =(MyDriveShell) shell();
    	long token = sh.getToken();
        if (args.length < 1) {
            throw new RuntimeException("USAGE: "+name()+" <path> [<text>]");
        }
        if (args.length == 2) {
            new WriteFileService(token, args[0], args[1]).execute();
        }
        if (args.length == 1) {
            new WriteFileService(token, args[0], null).execute();
        }
    }
    
}
