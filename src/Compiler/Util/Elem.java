package Compiler.Util;

import java.util.HashMap;
import java.util.Map;

public class Elem {
    private Map<String, Object> values = new HashMap<>();


    public void set(String name, Object value){
        values.put(name,value);
    }

    public Object get(String name){
        return values.get(name);
    }

    @Override
    public String toString() {
    return values.toString();
    }
}
