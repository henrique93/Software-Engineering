package pt.ulisboa.tecnico.es16al_28.domain;


import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyDrive extends MyDrive_Base {
	static final Logger log = LogManager.getRootLogger();	

	public static MyDrive getInstance() {
        MyDrive app = FenixFramework.getDomainRoot().getMydrive();
        if (app != null)
	    return app;
	log.trace("new Drive");
        return new MyDrive();
    }

    public MyDrive() {
        setRoot(FenixFramework.getDomainRoot());
    }
	
    public void cleanup() {
        for (User u: getUserSet()) {
            u.remove();
        }
    }

}
