package pt.ulisboa.tecnico.es16al_28.service;

import pt.ulisboa.tecnico.es16al_28.exception.InvalidTypeException;
import pt.ulisboa.tecnico.es16al_28.exception.TokenDoesNotExistException;
import pt.ulisboa.tecnico.es16al_28.exception.FileAlreadyExistsException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotCreateWithoutContentException;
import pt.ulisboa.tecnico.es16al_28.exception.CannotCreateWithContentException;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.domain.PlainFile;
import pt.ulisboa.tecnico.es16al_28.domain.App;
import pt.ulisboa.tecnico.es16al_28.domain.Dir;
import pt.ulisboa.tecnico.es16al_28.domain.Link;


public class CreateFileService extends MyDriveService {
  
	private Long _token;
	private String _name;	
	private String _type;
	private String _content;
	private String createFile;
	

	public CreateFileService(Long token, String name, String type) throws CannotCreateWithoutContentException {
        if (type.toLowerCase().equals("link")) {
            throw new CannotCreateWithoutContentException();
        }
        _token = token;
		_name = name;
		_type = type;

	}

	public CreateFileService(Long token, String name, String type, String content) throws CannotCreateWithContentException {
        if (type.toLowerCase().equals("dir")) {
            throw new CannotCreateWithContentException();
        }
		_token = token;
		_name = name;
		_type = type;
		_content = content;
		
	}

	@Override
	public final void dispatch() throws TokenDoesNotExistException, InvalidTypeException, FileAlreadyExistsException {	//FIXME: possible permissionException
        MyDrive mydrive = getMyDrive();
        Login l = mydrive.getLoginByToken(_token);
        if (l.getCurrentDir().directoryHasFile(_name)) {
            throw new FileAlreadyExistsException(_name);
        }
		switch (_type.toLowerCase()){
			case "app":		new App(l, _name, _content);
						break;
			case "link":		new Link(l, _name, _content);
						break;
			case "plainfile":	new PlainFile(l, _name, _content);
						break;
			case "dir":		new Dir(l, _name);
						break;
			default:		throw new InvalidTypeException(_type);

        }
	}

}
