package Compiler.Grammar;

import java.util.List;

public abstract class Query {
    public interface Visitor<R> {
       public R visitPlayQuery(Play query);
    }
   public static class Play extends Query {
        public Play(Token name, Expr.Match matching, Token moveName) {
            this.name = name;
            this.matching = matching;
            this.moveName = moveName;
        }

        public final Token name;
        public final Expr.Match matching;
        public final Token moveName;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPlayQuery(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
