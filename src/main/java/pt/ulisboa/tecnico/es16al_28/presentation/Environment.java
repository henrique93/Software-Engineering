package pt.ulisboa.tecnico.es16al_28.presentation;

import pt.ulisboa.tecnico.es16al_28.service.AddVariableService;
import pt.ulisboa.tecnico.es16al_28.presentation.MyDriveShell;
import java.util.Collections;
import java.util.HashMap;


public class Environment extends MyDriveCommand {

    public Environment(MyDriveShell sh) {
        super(sh, "env", "Create or change environment variable");
    }

    public void execute(String args[]) {
        MyDriveShell sh = (MyDriveShell) shell();
    	long token = sh.getToken();
	HashMap<String, String> map = sh.getEnvVarList();

	if (args.length == 0) {
		for (HashMap.Entry<String, String> entry : map.entrySet()){
			
			 System.out.println(entry.getKey() + "=" + entry.getValue());
		} 
	}

	if (args.length == 1) {
			 System.out.println(sh.getEvValue(args[0]));
	}

	if (args.length == 2) {
		AddVariableService service = new AddVariableService(token, args[0], args[1]);
		service.execute();
	}
    }
    
}
