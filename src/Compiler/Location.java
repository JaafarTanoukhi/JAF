
package Compiler;

public class Location {
    private String filePath; 
    private int line;
    private int col;
    private int character;

    public Location(String filePath){
        this.filePath = filePath;
        this.line = 0;
        this.col = 0;
        this.character = 0;
    }

    public Location(Location location){
        this.filePath = location.filePath;
        this.line = location.line;
        this.col = location.col;
        this.character = location.character;
    }

    public void incChar(){
        this.character++;
        this.col++;
    }

    public void incLine(){
        this.col = 0;
        this.line++;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public int getCharacter(){
        return character;
    }

    public void reset(){
        this.line = 0;
        this.col = 0;
        this.character = 0;
    }

    
}
