package Core;

import java.util.function.Supplier;

public class Event {
    public Supplier<String> test;

    public Event(){
        test = this::method;
    }

   public String method(){
    return "jana is cute";
   }
}
