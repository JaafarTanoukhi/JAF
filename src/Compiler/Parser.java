package Compiler;

import java.util.ArrayList;
import java.util.List;

import Compiler.Grammar.Action;
import Compiler.Grammar.Block;
import Compiler.Grammar.Expr;
import Compiler.Grammar.Query;
import Compiler.Grammar.Token;
import Compiler.Grammar.TokenType;
import Compiler.Grammar.Util;
import Compiler.Util.ErrorFormatter;
import Compiler.Grammar.Condition;

import static Compiler.Grammar.TokenType.*;

public class Parser {
    private class ParseError extends RuntimeException {
    }

    private List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public List<Block> parse() {
        try {
            List<Block> blocks = new ArrayList<>();
            while (!isAtEnd()) {
                blocks.add(block());
            }
            return blocks;
        } catch (ParseError error) {
            System.out.println(error);
            return null;
        }
    }

    private Block block() {
        expect(BLOCK_START, "Block must start with #");
        if(match(PLAYING)) 
        return playing();
        if (match(PLAYER))
            return player();
        if (match(GAME))
            return game();
        if (match(PROP))
            return prop();
        if (match(EVENT))
            return event();

        throw error(peek(), "Expected Start of Block");
    }

    private Block.Playing playing(){
        List<Query.Play> plays = new ArrayList<>();
        while(match(PLAY)){
            plays.add(play());
        }
        return new Block.Playing(plays);
    }

    private Block.Player player() {
        expect(IDENTIFIER, "Expected IDENTIFIER after player block start");
        Token name = previous();
        List<Token> state = varList();
        List<Action> actions = actionList();

        return new Block.Player(name, state, actions);
    }

    private Block.Game game() {
        List<Token> state = varList();
        List<Action> actions = actionList();

        return new Block.Game(state, actions);
    }

    private Block.Prop prop() {
        expect(IDENTIFIER, "Expected IDENTIFIER after prop block start");
        Token name = previous();

        List<Token> state = varList();
        List<Block.Move> move = moves();

        return new Block.Prop(name, state, move);
    }

    private Block.Event event() {
        expect(IDENTIFIER, "Expected IDENTIFIER after event block start");
        Token name = previous();
        List<Condition.ConditionGroup> conditionGroups = new ArrayList<>();

        expect(CONDITION, "Event block must have condition");
        conditionGroups.add(new Condition.ConditionGroup(condition()));

        while(match(CONDITION)){
            conditionGroups.add(new Condition.ConditionGroup(condition()));
        }

        expect(DO, "Event block must have do");
        List<Action> actions = actionList();

        return new Block.Event(name, conditionGroups, actions);
    }

    private Query.Play play(){
        expect(IDENTIFIER, "play query must be followed with IDENTIFIER");
        Token name = previous();

        Expr.Match matching = matching();

        expect(IDENTIFIER, "name of move to be played must be provided in query");
        Token moveName = previous();

        return new Query.Play(name, matching, moveName);
    }

    private List<Block.Move> moves() {
        List<Block.Move> moves = new ArrayList<>();

        while (match(MOVE)) {
            moves.add(move());
        }
        return moves;
    }

    private Block.Move move() {
        expect(IDENTIFIER, "Expected IDENTIFIER after Move Block Start");
        Token name = previous();

        List<Condition.ConditionGroup> conditionGroups = new ArrayList<>();
        while(match(CONDITION)){
            conditionGroups.add(new Condition.ConditionGroup(condition()));
        }
        expect(DO, "Move block must have do");
        List<Action> actions = actionList();

        return new Block.Move(name, conditionGroups, actions);
    }

    private List<Condition.Match> condition(){
        List<Condition.Match> matchs = new ArrayList<>();
        while(match(LISTING)){
            expect(IDENTIFIER, "condition listing must start with IDENTIFIER");
            matchs.add(new Condition.Match(previous(),matching()));
        }
        return matchs;
    }

    private List<Token> varList() {
        List<Token> state = new ArrayList<Token>();
        while (match(IDENTIFIER)) {
            state.add(previous());
            if (!match(COMMA))
                break;
        }
        return state;
    }

    private List<Action> actionList() {
        List<Action> actions = new ArrayList<Action>();
        while (match(LISTING)) {
            advance();
            switch (previous().type) {
                case CREATE:
                    actions.add(createAct());
                    break;
                case UPDATE:
                    actions.add(updateAct());
                    break;
                case TRANSFER:
                    actions.add(transferAct());
                    break;
                case DESTROY:
                    actions.add(destroyAct());
                    break;
                case SWITCH:
                    actions.add(switchAct());
                    break;

                default:
                    throw error(previous(), "This is not a supported action");
            }
        }

        return actions;
    }

    private Action.Create createAct() {
        expect(IDENTIFIER, "Expected IDENTIFIER after action declaration");

        Token name = previous();
        Action.Assign assignment = assignment();

        return new Action.Create(name, assignment);
    }

    private Action updateAct() {
        advance();
        switch (previous().type) {
            case THIS:
                return updateThis();
            case IDENTIFIER:
                return updateMatch();

            default:
                throw error(previous(), "Unexpected token after update action start");
        }

    }

    private Action.UpdateThis updateThis() {
        return new Action.UpdateThis(assignment());
    }

    private Action.UpdateMatch updateMatch() {
        Token name = previous();
        Expr.Match matching = matching();
        Action.Assign assignment = assignment();

        return new Action.UpdateMatch(name, matching, assignment);
    }

    private Action.Transfer transferAct() {

        expect(IDENTIFIER, "Expected IDENTIFIER after action declaration");
        Token name = previous();

        Expr.Match matching = matching();

        expect(IDENTIFIER, "Expected IDENTIFIER after matching in Transfer action");
        Token to = previous();

        return new Action.Transfer(name, matching, to);
    }

    private Action.Destroy destroyAct() {
        expect(IDENTIFIER, "Expected IDENTIFIER after action declaration");
        Token name = previous();

        Expr.Match matching = matching();

        return new Action.Destroy(name, matching);

    }

    private Action.Switch switchAct() {
        Token what;
        if (match(LOCKED, UNLOCKED)) {
            what = previous();
        } else {
            throw error(previous(), "Expected LOCKED or UNLOCKED after Switch start");
        }

        return new Action.Switch(what);
    }

    private Action.Assign assignment() {
        expect(OPEN_BRACKET, "Matching expression must start with open bracket");

        List<Util.Binding> bindings = new ArrayList<>();
        do {
            Util.Binding binding = binding();
            bindings.add(binding);
        } while (match(COMMA));

        expect(CLOSE_BRACKET, "Matching expression must end with closed bracket");

        return new Action.Assign(bindings);
    }

    private Util.Binding binding() {
        expect(IDENTIFIER, "Binding must start with an identifier");
        Token name = previous();
        expect(EQUAL, "Binding must have equal after identifier");
        Expr Expr = expression();

        return new Util.Binding(name, Expr);
    }

    private Expr.Match matching() {
        expect(OPEN_BRACKET, "Matching expression must start with open bracket");

        List<Expr> expressions = new ArrayList<>();
        do {
            Expr expr = expression();
            expressions.add(expr);
        } while (match(COMMA));

        expect(CLOSE_BRACKET, "Matching expression must end with closed bracket");

        return new Expr.Match(expressions);
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr left = comparison();
        while (match(EQUAL, BANG_EQUAL)) {
            left = new Expr.Binary(left, previous(), equality());
        }
        return left;
    }

    private Expr comparison() {
        Expr left = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            left = new Expr.Binary(left, previous(), term());
        }
        return left;
    }

    private Expr term() {
        Expr left = factor();
        while (match(PLUS, MINUS)) {
            left = new Expr.Binary(left, previous(), factor());
        }
        return left;
    }

    private Expr factor() {
        Expr left = unary();
        while (match(TIMES, DIVIDE, MODULUS)) {
            left = new Expr.Binary(left, previous(), unary());
        }

        return left;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            return new Expr.Unary(previous(), unary());
        }
        return primary();
    }

    private Expr primary() {

        if(match(IDENTIFIER)) 
            return new Expr.Var(previous());
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(STRING, NUMBER))
            return new Expr.Literal(previous().literal);
        if (match(OPEN_BRACKET)) {
            Expr expr = expression();
            expect(CLOSE_BRACKET, "Expected ) at the end of expression");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private void expect(TokenType type, String messageIfWrong) {
        if (match(type))
            return;
        throw error(peek(), messageIfWrong);
    }

    private ParseError error(Token token, String errorMessage) {
        ErrorFormatter.error(token, errorMessage);
        return new ParseError();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private void advance() {
        if (!isAtEnd())
            ++current;
    }

    private boolean check(TokenType type) {
        return tokens.get(current).type == type;
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }
}
