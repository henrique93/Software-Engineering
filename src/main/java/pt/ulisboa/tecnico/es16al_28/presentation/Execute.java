package pt.ulisboa.tecnico.es16al_28.presentation;

import pt.ulisboa.tecnico.es16al_28.service.ExecuteFileService;
import pt.ulisboa.tecnico.es16al_28.presentation.MyDriveShell;
import java.util.*;

public class Execute extends MyDriveCommand {

    public Execute(MyDriveShell sh) {
        super(sh, "do path", "execute file");
    }

    public void execute(String args[]) {
    	MyDriveShell sh = (MyDriveShell) shell();
    	long token = sh.getToken();
    	int n = args.length;

        if (args.length < 1) {
            throw new RuntimeException("USAGE: " + name() + " <path> [<args>]");
        }
        if (args.length == 1) {
            ExecuteFileService exe = new ExecuteFileService(token, args[0], null);
            exe.execute();
        }
        else {  
        	String realArgs[] = java.util.Arrays.copyOfRange(args, 1, n+1);
        	ExecuteFileService exe = new ExecuteFileService(token, args[0], realArgs);
        	exe.execute();
        }
        
    } 
    
}
