package com.simulacion;

import com.simulacion.eventos.Event;

import java.util.LinkedList;
import java.util.List;

public class EventHandler {
    private List<EventEntry> eventList;
    private CPU cpu;

    private static final EventHandler INSTANCE = new EventHandler();
    public static EventHandler getInstance(){return INSTANCE;}

    private EventHandler(){
        this.eventList = new LinkedList<>();
    }

    public void addEvent(Event event){
        eventList.add(new EventEntry(cpu.getClock() + event.cycles, event));
    }

    public Event fireEvent(long clock){
        return null;
    }

    private class EventEntry{
        public long eventTime;
        public Event event;

        public EventEntry(long eventTime, Event event) {
            this.eventTime = eventTime;
            this.event = event;
        }
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }
}
