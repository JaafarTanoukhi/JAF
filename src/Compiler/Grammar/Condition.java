package Compiler.Grammar;

import java.util.List;

public abstract class Condition {
    public interface Visitor<R> {
       public R visitMatchCondition(Match condition);
    }
   public static class Match extends Condition {
        public Match(Token name, Expr.Match condition) {
            this.name = name;
            this.condition = condition;
        }

        public final Token name;
        public final Expr.Match condition;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitMatchCondition(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
