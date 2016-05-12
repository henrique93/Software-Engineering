package pt.ulisboa.tecnico.es16al_28.presentation;

import java.util.*;

import pt.ulisboa.tecnico.es16al_28.service.ListDirectoryService;
import pt.ulisboa.tecnico.es16al_28.service.ChangeDirectoryService;

import pt.ulisboa.tecnico.es16al_28.service.dto.FileDto;

public class List extends MyDriveCommand {

    public List(MyDriveShell sh) {
        super(sh, "ls", "List directory");
    }

    public void execute(String args[]) {
        MyDriveShell sh = (MyDriveShell) shell();
        long token = sh.getToken();
        
        if (args.length == 0) {
            ListDirectoryService lds = new ListDirectoryService(token);
            lds.execute();
        
            for (FileDto d : lds.result()){
                System.out.println(d.toString());
            }
        
        }
        else{
            //guarda dir actual
            ChangeDirectoryService currentDir = new ChangeDirectoryService(token, ".");
            currentDir.execute();
            String current = currentDir.result();
            
            //muda de dir
            ChangeDirectoryService change =  new ChangeDirectoryService(token, args[0]);
            change.execute();
            
            //lista
            ListDirectoryService ldsCurrent = new ListDirectoryService(token);
            ldsCurrent.execute();
            
            for (FileDto f : ldsCurrent.result()){
                System.out.println(f.toString());
            }
            
            //muda para o dir actual
            ChangeDirectoryService changeBack =  new ChangeDirectoryService(token, current);
            change.execute();
        }
        
        
    }
    
}
