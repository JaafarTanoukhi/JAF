package Compiler.Util;

import java.util.List;

public class VarResult{

    public final String var;
    public final List<Elem> elements;

    public VarResult(String var, List<Elem> elements){
        this.var = var;
        this.elements = elements;
    }

}