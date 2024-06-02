package Compiler.Util;

import java.util.HashMap;
import java.util.Map;

import Compiler.Grammar.Token;

public class Enviroment {
    private final Map<String, Object> values = new HashMap<String, Object>();

    public void define(String name, Object value){
        values.put(name, value);
    }

    public Object get(Token name){
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

}
