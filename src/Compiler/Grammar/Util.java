package Compiler.Grammar;

import java.util.List;

public abstract class Util {
    public interface Visitor<R> {
       public R visitBindingUtil(Binding util);
    }
   public static class Binding extends Util {
        public Binding(Token name, Expr expression) {
            this.name = name;
            this.expression = expression;
        }

        public final Token name;
        public final Expr expression;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBindingUtil(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
