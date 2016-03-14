package pt.ulisboa.tecnico.es16al_28.domain;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile () {
        super();
    }
    
    public PlainFile(int id, String name, String permission, String owner, Dir dir) {
        super();
        init(id, name, permission, owner, dir);
    }

    /**
     *  Reads the content from a file
     *  @param file     file to read the content from
     *  @return string  file's content
     */
    public String readFile() {
        return this.getApp();
    }

}
