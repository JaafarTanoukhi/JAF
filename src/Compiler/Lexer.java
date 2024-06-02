package Compiler;


import static Compiler.Grammar.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Compiler.Grammar.Token;
import Compiler.Grammar.TokenType;
import Compiler.Util.ErrorFormatter;
import Compiler.Util.FileDependencyResolver.FileContent;

public class Lexer {

    private String source;
    private Location currLocation;
    private int start;
    private ArrayList<Token> tokens;
    private ArrayList<String> errors;
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<String, TokenType>();

        keywords.put("yes", TRUE);
        keywords.put("no", FALSE);

        keywords.put("Playing",PLAYING);
        keywords.put("Prop", PROP);
        keywords.put("Game", GAME);
        keywords.put("Player", PLAYER);
        keywords.put("Event", EVENT);
        
        keywords.put("play",PLAY);
        keywords.put("create", CREATE);
        keywords.put("transfer", TRANSFER);
        keywords.put("update", UPDATE);
        keywords.put("destroy", DESTROY);
        keywords.put("switch", SWITCH);
        keywords.put("this",THIS);

        keywords.put("LOCKED", LOCKED);
        keywords.put("UNLOCKED", UNLOCKED);

        keywords.put("Move",MOVE);
        keywords.put("Condition", CONDITION);
        keywords.put("Do", DO);
    }

    public Lexer(String line){
        this.source = line;
        this.currLocation = new Location("--CONSOLE--");

        this.start = 0;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();

    }

    public Lexer(FileContent content) {
        this.source = content.getContent();
        this.currLocation = new Location(content.getFilePath());

        this.start = 0;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public void tokenize() {
        while (!done()) {
            start = currLocation.getCharacter();
            nextToken();
        }
        tokens.add(new Token(EOF, "", null, currLocation));
    }

    private void nextToken() {
        char character = advance();
        switch (character) {
            case '#':
                addToken(BLOCK_START);
                break;
            case '|':
                addToken(STATEMENT_START);
                break;
            case '~':
                addToken(LISTING);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '*':
                addToken(TIMES);
                break;
            case '/':
                addToken(DIVIDE);
                break;
            case '%':
                addToken(MODULUS);
                break;
            case '=':
                addToken(EQUAL);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '(':
                addToken(OPEN_BRACKET);
                break;
            case ')':
                addToken(CLOSE_BRACKET);
                break;
            case '{':
                addToken(OPEN_CARTESIAN);
                break;
            case '}':
                addToken(CLOSE_CARTESIAN);
                break;
            case '[':
                addToken(OPEN_PAIRING);
                break;
            case ']':
                addToken(CLOSE_PAIRING);
                break;

            case '!':
            if(match('=')) addToken(BANG_EQUAL); else addToken(BANG);
                break;
            case '>':
            if(match('=')) addToken(GREATER_EQUAL); else addToken(GREATER);
                break;
            case '<':
            if(match('=')) addToken(LESS_EQUAL); else addToken(LESS);
                break;

            case ' ':
                break;
            case '\r':
                break;
            case '\t':
                break;

            case '\n':
                currLocation.incLine();
                break;
            case '"':
                string();
                break;

            default:
                if (isDigit(character)) {
                    number();
                } else if (isAlpha(character)) {
                    identifier();
                } else {
                    logError("Unexpected character");
                }
        }
    }

    private void string() {
        while(peek()!='"' && !done()){
            if(peek() == '\n') currLocation.incLine();
            advance();
        }

        if(done()){
            logError("Unterminated string.");
            return;
        }
        //consume the closing "
        advance();

        String value = source.substring(start + 1, currLocation.getCharacter() -1);
        addToken(STRING, value);
    }

    private void number() {
        while (isDigit(peek()))
            advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek()))
                advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, currLocation.getCharacter())));
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();

        String text = source.substring(start, currLocation.getCharacter());
        TokenType type = keywords.get(text);
        if (type == null)
            type = IDENTIFIER;

        addToken(type);

    }

    private char peek() {
        if (done())
            return '\0';
        return source.charAt(currLocation.getCharacter());
    }

    private char peekNext() {
        if (currLocation.getCharacter() + 1 >= source.length())
            return '\0';
        return source.charAt(currLocation.getCharacter() + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    private char advance() {
        currLocation.incChar();
        return source.charAt(currLocation.getCharacter() - 1);
    }

    private boolean match(char expected) {
        if (done())
            return false;
        if (source.charAt(currLocation.getCharacter()) != expected)
            return false;

        advance();
        return true;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, currLocation.getCharacter());
        tokens.add(new Token(type, text, literal, currLocation));
    }

    private boolean done() {
        return currLocation.getCharacter() >= source.length();
    }

    private void logError(String message) {
        errors.add(ErrorFormatter.format(message, currLocation));
    }

    public Location getCurrLocation() {
        return new Location(currLocation);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

}
