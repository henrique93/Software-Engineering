package pt.ulisboa.tecnico.es16al_28.service.dto;

public class ListDto implements Comparable<ListDto>{
    
    private String name;
    private String owner;
    private String lastChange;
    private String permission;
    private java.lang.Integer id;
    
    public ListDto (java.lang.Integer id, String name, String owner, String permission, String lastChange){
        this.name = name;
        this.lastChange = lastChange;
        this.permission = permission;
        this.id = id;
        this.owner = owner;
    }
    
    public final String getName(){
        return this.name;
    }
    
    public final String getLastChange(){
        return this.lastChange;
    }
    
    public final String getPermission(){
        return this.permission;
    }
    
    public final java.lang.Integer getId(){
        return this.id;
    }
    
    public final String getOwner(){
        return this.owner;
    }
    
    @Override
    public int compareTo(ListDto other) {
        return getName().compareTo(other.getName());
    }
}