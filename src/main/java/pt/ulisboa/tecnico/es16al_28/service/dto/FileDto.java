package pt.ulisboa.tecnico.es16al_28.service.dto;

public class FileDto implements Comparable<FileDto>{
    
    private String _name;
    private String _owner;
    private String _lastChange;
    private String _permission;
    private java.lang.Integer _id;
    
    public FileDto (java.lang.Integer id, String name, String owner, String permission, String lastChange){
        _name = name;
        _lastChange = lastChange;
        _permission = permission;
        _id = id;
        _owner = owner;
    }
    
    public final String getName(){
        return _name;
    }
    
    public final String getLastChange(){
        return _lastChange;
    }
    
    public final String getPermission(){
        return _permission;
    }
    
    public final java.lang.Integer getId(){
        return _id;
    }
    
    public final String getOwner(){
        return _owner;
    }
    
    @Override
    public int compareTo(FileDto other) {
        return getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return "File ID: " + _id + "\tName: " + _name + "\tOwner: " + _owner + "\tPermisisons: " + _permission + "\tModified last at: " + _lastChange;
    }


}