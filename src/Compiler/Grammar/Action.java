package Compiler.Grammar;

import java.util.List;

public abstract class Action {
    public interface Visitor<R> {
       public R visitCreateAction(Create action);
       public R visitUpdateMatchAction(UpdateMatch action);
       public R visitUpdateThisAction(UpdateThis action);
       public R visitTransferAction(Transfer action);
       public R visitDestroyAction(Destroy action);
       public R visitSwitchAction(Switch action);
       public R visitAssignAction(Assign action);
    }
   public static class Create extends Action {
        public Create(Token name, Action.Assign assignment) {
            this.name = name;
            this.assignment = assignment;
        }

        public final Token name;
        public final Action.Assign assignment;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCreateAction(this);
        }
    }

   public static class UpdateMatch extends Action {
        public UpdateMatch(Token name, Expr.Match matching, Action.Assign assignment) {
            this.name = name;
            this.matching = matching;
            this.assignment = assignment;
        }

        public final Token name;
        public final Expr.Match matching;
        public final Action.Assign assignment;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUpdateMatchAction(this);
        }
    }

   public static class UpdateThis extends Action {
        public UpdateThis(Action.Assign assignment) {
            this.assignment = assignment;
        }

        public final Action.Assign assignment;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUpdateThisAction(this);
        }
    }

   public static class Transfer extends Action {
        public Transfer(Token name, Expr.Match matching, Token to) {
            this.name = name;
            this.matching = matching;
            this.to = to;
        }

        public final Token name;
        public final Expr.Match matching;
        public final Token to;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitTransferAction(this);
        }
    }

   public static class Destroy extends Action {
        public Destroy(Token name, Expr.Match matching) {
            this.name = name;
            this.matching = matching;
        }

        public final Token name;
        public final Expr.Match matching;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitDestroyAction(this);
        }
    }

   public static class Switch extends Action {
        public Switch(Token what) {
            this.what = what;
        }

        public final Token what;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitSwitchAction(this);
        }
    }

   public static class Assign extends Action {
        public Assign(List<Util.Binding> bindings) {
            this.bindings = bindings;
        }

        public final List<Util.Binding> bindings;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignAction(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
