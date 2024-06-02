package Compiler.Grammar;

import Compiler.Location;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final Location location;


    public Token(){
        this.type = null;
        this.lexeme = null;
        this.literal = null;
        this.location = null;
    }

    public Token(TokenType type, String lexeme, Object literal, Location location) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.location = new Location(location);
    }

    public String toString(){
        return type + " " + lexeme + " " + literal;
    }

    
}
