package Core;

import java.util.HashMap;

public class EventDispatcher {
    public HashMap<String, Event> eventMap;

    public EventDispatcher(){
        this.eventMap = new HashMap<String, Event>();
    }

    
    
}
