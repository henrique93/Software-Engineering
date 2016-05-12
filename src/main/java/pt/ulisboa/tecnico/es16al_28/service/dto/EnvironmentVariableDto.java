package pt.ulisboa.tecnico.es16al_28.service.dto;

public class EnvironmentVariableDto implements Comparable<EnvironmentVariableDto> {

    private String _name;
    private String _value;
    
    public EnvironmentVariableDto (String name, String value) {
        _name = name;
        _value = value;
    }

    public final String getName() {
        return _name;
    }

    public final String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "Variable name: " + _name + "value: " + _value;
    }
    
    @Override
    public int compareTo(EnvironmentVariableDto other) {
        return getName().compareTo(other.getName());
    }


}