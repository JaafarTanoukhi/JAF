package Compiler.Util;

import static Compiler.Grammar.TokenType.IDENTIFIER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Compiler.Grammar.Action;
import Compiler.Grammar.Block.Move;
import Compiler.Grammar.Condition;
import Compiler.Grammar.Expr.Match;
import Compiler.Grammar.Token;

public class Context {
    private String currentContext = "";
    private Map<String, List<Elem>> objectsMap = new HashMap<>();
    private Map<String, Map<String,Move>> movesMap = new HashMap<>();
    private List<Move> eventMoves = new ArrayList<>();
    public Elem currElement;
    private Venn<Object> thisElements;
    
    public void defineBlock(String name){
        objectsMap.put(name, new ArrayList<Elem>());
        movesMap.put(name, new HashMap<String, Move>());
        currentContext = name;
    }

    public void createElement(Elem element){
        if(currentContext == "") throw new RuntimeError("Invalid context while creating element");
        objectsMap.get(currentContext).add(element);
    }

    public void defineMove(Move move){
        if(currentContext == "") throw new RuntimeError("Invalid context while defining move");
        movesMap.get(currentContext).put(move.name.lexeme,move);
    }

    public void defineEvent(List<Condition.Match> condition, List<Action> actions){
        if(currentContext == "") throw new RuntimeError("Invalid context while defining event");
        eventMoves.add(new Move(new Token(),condition,actions));
    }

    public void set(String name){
        currentContext = name;
    }

    public void setThis(Venn<Object> elements){
        thisElements = elements;
    }

    public VarResult getElements(String var){
        if(currentContext == "") throw new RuntimeError("Invalid context while getting elements");
        return new VarResult(var, objectsMap.get(currentContext));
    }

    public Venn<Object> thisElements(){
        return thisElements;
    }

    public Move getMove(String moveName){
        return movesMap.get(currentContext).get(moveName);
    }

    public List<Move> getEvents(){
        return this.eventMoves;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Context {");
        sb.append("\n  currentContext: ").append(currentContext);
        sb.append(",\n  objectsMap: ").append(objectsMap);
        sb.append(",\n  movesMap: ").append(movesMap);
        sb.append(",\n  eventMoves: ").append(eventMoves);
        sb.append(",\n  currElement: ").append(currElement);
        sb.append(",\n  thisElements: ").append(thisElements);
        sb.append("\n}");
        return sb.toString();
    }

}
