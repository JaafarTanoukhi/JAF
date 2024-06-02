// if you are reading this comment then im stupid and smelly and i didnt look at all the lines of my code
package Compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Compiler.Grammar.Block;
import Compiler.Grammar.Expr;
import Compiler.Grammar.Token;
import Compiler.Grammar.TokenType;
import Compiler.Util.FileDependencyResolver;
import Compiler.Util.FileDependencyResolver.FileContent;

public class JAF {
    public static void main(String[] args) throws Exception {
        // runFile();
        test();
    }

    // private static void runPrompt() throws IOException {
    //     InputStreamReader input = new InputStreamReader(System.in);
    //     BufferedReader reader = new BufferedReader(input);

    //     for (;;) {
    //         System.out.print("> ");
    //         String line = reader.readLine();
    //         if (line == null)
    //             break;
    //         run(line);
            
    //     }
    // }

    // private static void run(String source) {
    //     Lexer lexer = new Lexer(source);
    //     lexer.tokenize();
    //     List<Token> tokens = lexer.getTokens();

    //     Parser parser = new Parser(tokens);

    //     Expr statements = parser.parse();

    //     System.out.println(compiler.eval(statements));

    // }

    private static void test() throws IOException {
        Interface inter = new Interface();
       System.out.println(inter.Query("play cell (rowNumber = 1) fillX"));
    }

    private static List<Token> runLexer(){
        

        List<Token> tokens = new ArrayList<>();
        String content = new Scanner(System.in).nextLine();
        Lexer lexer = new Lexer(content);
        lexer.tokenize();

        ArrayList<String> errors = lexer.getErrors();
        if (errors.size() > 0) {
            System.out.println("\u001B[31m");
            for (String error : errors) {
                System.out.println(error);
                System.exit(65);
            }
        }
        tokens.addAll(lexer.getTokens());

        return tokens;
    }

    private static void runFile()  throws IOException {
        FileDependencyResolver resolver = new FileDependencyResolver();
        ArrayList<FileContent> contents = resolver.resolveDependencies("src\\TEST\\game.python");

        ArrayList<Token> tokens = new ArrayList<>();
        for(FileContent content : contents){
            Lexer lexer = new Lexer(content);
            lexer.tokenize();
            tokens.addAll(lexer.getTokens());
        }

        Parser parser = new Parser(tokens);
        List<Block> blocks = parser.parse();
        Compiler compiler = new Compiler();
        compiler.start(blocks);

    }
    
}
