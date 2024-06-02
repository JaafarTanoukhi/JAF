package Compiler.Util;

import Compiler.Grammar.Token;

public class RuntimeError extends  RuntimeException{
    public final Token token;
 

    public RuntimeError(String message){
        super(message);
        this.token = null;
    }

     public RuntimeError(Token token, String message){
         super(message);
         this.token = token;
     }
 }
