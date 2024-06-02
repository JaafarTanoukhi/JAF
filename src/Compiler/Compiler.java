package Compiler;

import Compiler.Grammar.Expr;
import Compiler.Grammar.Expr.Binary;
import Compiler.Grammar.Expr.Grouping;
import Compiler.Grammar.Expr.Literal;
import Compiler.Grammar.Expr.Match;
import Compiler.Grammar.Expr.Unary;
import Compiler.Grammar.Expr.Var;
import Compiler.Grammar.Util.Binding;
import Compiler.Util.Context;
import Compiler.Util.Elem;
import Compiler.Util.VarResult;
import Compiler.Util.Venn;
import Compiler.Grammar.Block;
import Compiler.Grammar.Block.Event;
import Compiler.Grammar.Block.Game;
import Compiler.Grammar.Block.Move;
import Compiler.Grammar.Block.Player;
import Compiler.Grammar.Block.Playing;
import Compiler.Grammar.Block.Prop;

import Compiler.Grammar.Action;
import Compiler.Grammar.Action.Assign;
import Compiler.Grammar.Action.Create;
import Compiler.Grammar.Action.Destroy;
import Compiler.Grammar.Action.Switch;
import Compiler.Grammar.Action.Transfer;
import Compiler.Grammar.Action.UpdateMatch;
import Compiler.Grammar.Action.UpdateThis;
import Compiler.Grammar.Query;
import Compiler.Grammar.Query.Play;
import Compiler.Grammar.Condition;

import java.util.List;

import java.util.ArrayList;

public class Compiler implements Expr.Visitor<Object>, Block.Visitor<Void>,Action.Visitor<Void>,Query.Visitor<Object>, Condition.Visitor<Boolean>{

    public Context context = new Context();

    public Compiler() {
    }

    public void start(List<Block> blocks){
        for(Block block : blocks){
            defineBlock(block);
        }
    }

    private void defineBlock(Block block){
        block.accept(this);
    }

    
    @Override
    public Void visitPlayingBlock(Playing block) {
        for(Query.Play play : block.plays){
            executeQuery(play);
        }
        return null;
    }

    @Override
    public Void visitPlayerBlock(Player block) {
      context.defineBlock(block.name.lexeme);
    //   block.state this is not needed for now
      for(Action action : block.createActs){
        executeAction(action);
      }
      return null;
    }



    @Override
    public Void visitGameBlock(Game block) {
        //   block.state this is not needed for now
          for(Action action : block.createActs){
            executeAction(action);
          }
          return null;
    }



    @Override
    public Void visitPropBlock(Prop block) {
        context.defineBlock(block.name.lexeme);
        //   block.state this is not needed for now
          for(Move move : block.moves){
            defineBlock(move);
          }
          return null;
    }

    @Override
    public Void visitEventBlock(Event block) {
        context.defineBlock(block.name.lexeme);
        context.defineEvent(block.condition,block.actions);
        return null;
    }



    @Override
    public Void visitMoveBlock(Move block) {
       context.defineMove(block);
       return null;
    }

    public Object executeQuery(Query query){
        return query.accept(this);
    }

    @Override
    public Object visitPlayQuery(Play query) {
      context.set(query.name.lexeme);
      
      Object obj = eval(query.matching);
      context.setThis((Venn<Object>)obj);
      
      Move move = context.getMove(query.moveName.lexeme);

      if(checker(move.condition)){
          for(Action action : move.actions){
              executeAction(action);
            }
        }

      for(Move m : context.getEvents()){
        if(checker(m.condition)){
            for(Action action : m.actions){
                executeAction(action);
            }
        }
    }
      
      return context;
    }

    private Boolean checker(List<Condition.Match> conditions){
        for(Condition condition : conditions){
            if(!checkCondition(condition)) return false;
        }
        return true;
    }

    private Boolean checkCondition(Condition condition){
        return condition.accept(this);
    }

    @Override
    public Boolean visitMatchCondition(Condition.Match condition) {
        context.set(condition.name.lexeme);
        
        if(((Venn<Object>)eval(condition.condition)).isEmpty()) return false;
        
        return true;
    }


    private void executeAction(Action action){
        action.accept(this);
    }

    @Override
    public Void visitCreateAction(Create action) {
        context.set(action.name.lexeme);
        Elem element = new Elem();
        context.currElement = element;
        executeAction(action.assignment);
        context.createElement(element);
        return null;
    }



    @Override
    public Void visitUpdateMatchAction(UpdateMatch action) {
        context.set(action.name.lexeme);
        Venn<Object> venn =(Venn<Object>) eval(action.matching);
        for(Object obj : venn.getElements()){
            context.currElement = (Elem) obj;
            executeAction(action.assignment);
        }
        return null;
    }

    @Override
    public Void visitUpdateThisAction(UpdateThis action) {
        Venn<Object> venn = context.thisElements();
        for(Object obj : venn.getElements()){
            context.currElement = (Elem) obj;
            executeAction(action.assignment);
        }
        return null;
    }

    @Override
    public Void visitAssignAction(Assign action) {
        
        for(Binding binding : action.bindings){
            context.currElement.set(binding.name.lexeme,eval(binding.expression));
        }

        return null;
    }

    private Object eval(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitMatchExpr(Match expr) {
        Venn<Object> evals = new Venn<>(); 

        for(Expr e : expr.expressions){
            evals.add((List<Elem>)eval(e));
        }

        return evals;
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = eval(expr.left);
        Object right = eval(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                break;

            case MINUS:
                return (double) left - (double) right;
            case TIMES:
                return (double) left * (double) right;
            case MODULUS:
                return (double) left % (double) right;
            case DIVIDE:
                return (double) left / (double) right;
            case GREATER:
                return (double) left > (double) right;
            case GREATER_EQUAL:
                return (double) left >= (double) right;
            case LESS:
                return (double) left < (double) right;
            case LESS_EQUAL:
                return (double) left <= (double) right;
            case EQUAL:
                if(left instanceof VarResult && right instanceof Object){
                    VarResult result = (VarResult)left;
                    List<Elem> matchingElements = new ArrayList<>();
                    for(Elem elem : result.elements){
                        if((elem.get(result.var)).equals(right)){
                            matchingElements.add(elem);
                        }
                    }
                    return matchingElements;
                }
                return left == right;

            case BANG_EQUAL:
                return left != right;
            default:
                return null;
        }
        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return eval(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object result = eval(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return isTruthy(result);
            case MINUS:
                return -(double) (result);
            default:
                break;
        }

        // unreachable
        return null;
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitVarExpr(Var expr) {
        return context.getElements(expr.name.lexeme);
    }

    private boolean isTruthy(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Boolean)
            return (Boolean) obj;
        return true;
    }


    @Override
    public Void visitTransferAction(Transfer action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitTransferAction'");
    }



    @Override
    public Void visitDestroyAction(Destroy action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitDestroyAction'");
    }



    @Override
    public Void visitSwitchAction(Switch action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitSwitchAction'");
    }

    

}
