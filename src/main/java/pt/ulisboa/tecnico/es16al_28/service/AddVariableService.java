package pt.ulisboa.tecnico.es16al_28.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import pt.ulisboa.tecnico.es16al_28.domain.EnvironmentVariable;
import pt.ulisboa.tecnico.es16al_28.domain.Login;
import pt.ulisboa.tecnico.es16al_28.domain.MyDrive;
import pt.ulisboa.tecnico.es16al_28.service.dto.EnvironmentVariableDto;
import pt.ulisboa.tecnico.es16al_28.exception.EnvironmentVariableDoesNotExistException;

public class AddVariableService extends MyDriveService {

    private long _token;
    private String _name;
    private String _value;
    private List<EnvironmentVariableDto> EnvVarList = new ArrayList<EnvironmentVariableDto>();

    public AddVariableService (long token, String name, String value) {
        _token = token;
        _name = name;
        _value = value;
    }

    public List<EnvironmentVariableDto> result() {
        MyDrive mydrive = getMyDrive();
        Login login = mydrive.getLoginByToken(_token);
        for (EnvironmentVariable ev : login.getEnvVarSet()) {
            String evName = ev.getName();
            String evValue = ev.getValue();
            EnvironmentVariableDto evDto = new EnvironmentVariableDto(evName, evValue);
            EnvVarList.add(evDto);
        }
        Collections.sort(EnvVarList);
        return EnvVarList;
    }

    @Override
    public final void dispatch() {
        MyDrive mydrive = getMyDrive();
        Login login = mydrive.getLoginByToken(_token);
        try {
            EnvironmentVariable ev = login.getEvByName(_name);
            ev.updateEv(_value);
        } catch (EnvironmentVariableDoesNotExistException e) { new EnvironmentVariable(login, _name, _value); }
    }
    
    
}