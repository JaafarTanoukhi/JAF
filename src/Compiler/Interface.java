package Compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Compiler.Grammar.Block;
import Compiler.Grammar.Query.Play;
import Compiler.Grammar.Token;
import Compiler.Util.Context;
import Compiler.Util.FileDependencyResolver;
import Compiler.Util.FileDependencyResolver.FileContent;

public class Interface {
    Compiler compiler = new Compiler();
    
    public Interface(){
        try{
            FileDependencyResolver resolver = new FileDependencyResolver();
            List<Token> tokens = new ArrayList<>();
            for(FileContent content : resolver.resolveDependencies("src\\TEST\\game.py")){
                Lexer lexer = new Lexer(content);
                lexer.tokenize();
                tokens.addAll(lexer.getTokens());
            }
            
            Parser parser = new Parser(tokens);
            List<Block> blocks = parser.parse();
            
            compiler.start(blocks);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        }

    public Object Query(String query){
        String block = "#Playing " + query;
        Lexer lexer = new Lexer(block);
        lexer.tokenize();
        Parser parser = new Parser(lexer.getTokens());
        Block.Playing playing = (Block.Playing)(parser.parse()).get(0);

        Object result = null;

        for(Play play : playing.plays){
           result = compiler.executeQuery(play);
        }

        return result;
    }

    public Context getContext(){
        return compiler.context;
    }
    

   
}
