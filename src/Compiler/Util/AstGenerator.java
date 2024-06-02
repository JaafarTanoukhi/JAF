package Compiler.Util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class AstGenerator {
    public static void main(String[] args) throws Exception {
        String outputDir = "src\\Compiler\\Grammar";

        defineAst(outputDir, "Expr", Arrays.asList(
            "Match : List<Expr> expressions",
            "Binary : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
            "Unary : Token operator, Expr right",
            "Var : Token name"
        ));

        defineAst(outputDir,"Block",Arrays.asList(
            "Playing : List<Query.Play> plays",
            "Player : Token name, List<Token> state, List<Action> createActs",
            "Game : List<Token> state, List<Action> createActs",
            "Prop : Token name, List<Token> state, List<Block.Move> moves",
            "Event : Token name, List<Condition.Match> condition, List<Action> actions",
            "Move : Token name, List<Condition.Match> condition, List<Action> actions"
        ));

        defineAst(outputDir, "Util", Arrays.asList(
            "Binding : Token name, Expr expression"
        ));

        defineAst(outputDir, "Condition", Arrays.asList(
            "Match: Token name, Expr.Match condition"
        ));

        defineAst(outputDir,"Action", Arrays.asList(
            "Create :Token name, Action.Assign assignment",
            "UpdateMatch : Token name, Expr.Match matching, Action.Assign assignment",
            "UpdateThis : Action.Assign assignment",
            "Transfer : Token name, Expr.Match matching, Token to",
            "Destroy : Token name, Expr.Match matching",
            "Switch : Token what",
            "Assign : List<Util.Binding> bindings"
        ));

        defineAst(outputDir, "Query", Arrays.asList(
            "Play : Token name, Expr.Match matching, Token moveName"
        ));

    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package Compiler.Grammar;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();

        writer.println("public abstract class " + baseName + " {");
        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        
        writer.println();
        writer.println("    public abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");

        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("   public static class " + className + " extends " + baseName + " {");

        writer.println("        public " + className + "(" + fieldList + ") {");

        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }

        writer.println("        }");

        writer.println();
        for (String field : fields) {
            writer.println("        public final " + field + ";");
        }
        
        writer.println();
        writer.println("        @Override");
        writer.println("        public <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");
        
        writer.println("    }");
        writer.println();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    public interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("       public R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("    }");
    }
}
