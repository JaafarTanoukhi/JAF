package Compiler.Grammar;

import java.util.List;

public abstract class Block {
    public interface Visitor<R> {
       public R visitPlayingBlock(Playing block);
       public R visitPlayerBlock(Player block);
       public R visitGameBlock(Game block);
       public R visitPropBlock(Prop block);
       public R visitEventBlock(Event block);
       public R visitMoveBlock(Move block);
    }
   public static class Playing extends Block {
        public Playing(List<Query.Play> plays) {
            this.plays = plays;
        }

        public final List<Query.Play> plays;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPlayingBlock(this);
        }
    }

   public static class Player extends Block {
        public Player(Token name, List<Token> state, List<Action> createActs) {
            this.name = name;
            this.state = state;
            this.createActs = createActs;
        }

        public final Token name;
        public final List<Token> state;
        public final List<Action> createActs;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPlayerBlock(this);
        }
    }

   public static class Game extends Block {
        public Game(List<Token> state, List<Action> createActs) {
            this.state = state;
            this.createActs = createActs;
        }

        public final List<Token> state;
        public final List<Action> createActs;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGameBlock(this);
        }
    }

   public static class Prop extends Block {
        public Prop(Token name, List<Token> state, List<Block.Move> moves) {
            this.name = name;
            this.state = state;
            this.moves = moves;
        }

        public final Token name;
        public final List<Token> state;
        public final List<Block.Move> moves;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPropBlock(this);
        }
    }

   public static class Event extends Block {
        public Event(Token name, List<Condition.ConditionGroup> conditionGroup, List<Action> actions) {
            this.name = name;
            this.conditionGroup = conditionGroup;
            this.actions = actions;
        }

        public final Token name;
        public final List<Condition.ConditionGroup> conditionGroup;
        public final List<Action> actions;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitEventBlock(this);
        }
    }

   public static class Move extends Block {
        public Move(Token name, List<Condition.ConditionGroup> conditionGroup, List<Action> actions) {
            this.name = name;
            this.conditionGroup = conditionGroup;
            this.actions = actions;
        }

        public final Token name;
        public final List<Condition.ConditionGroup> conditionGroup;
        public final List<Action> actions;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitMoveBlock(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
