package Compiler.Grammar;

import java.util.List;

public abstract class Condition {
    public interface Visitor<R> {
       public R visitMatchCondition(Match condition);
       public R visitConditionGroupCondition(ConditionGroup condition);
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

   public static class ConditionGroup extends Condition {
        public ConditionGroup(List<Condition.Match> conditions) {
            this.conditions = conditions;
        }

        public final List<Condition.Match> conditions;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitConditionGroupCondition(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
