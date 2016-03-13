package pt.ulisboa.tecnico.es16al_28.domain;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
    
    public PlainFile(int id, String name, String lastChange, String permission, User owner, Dir dir) {
        super();
        init(id, name, lastChange, permission, owner, dir);
    }

     /**
     *  Reads the content from a file
     *  @param file     file to read the content from
     *  @return string  file's content
     */
    public String readFile(PlainFile file) {
        return file.getApp();
    }

}
