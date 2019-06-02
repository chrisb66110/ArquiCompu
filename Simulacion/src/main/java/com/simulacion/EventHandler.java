package com.simulacion;

import com.simulacion.eventos.Event;

import java.util.LinkedList;
import java.util.List;

public class EventHandler {
    private List<Event> eventList;

    private static final EventHandler INSTANCE = new EventHandler();
    public static EventHandler getInstance(){return INSTANCE;}

    private EventHandler(){
        this.eventList = new LinkedList<>();
    }

    public void addEvent(Event event){

    }

    public Event fireEvent(long clock){
        return null;
    }
}
