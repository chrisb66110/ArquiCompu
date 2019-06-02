package com.simulacion.eventos;

public class Event {
    public long eventTime;
    public int cycles;
    public String type;
    public Object[] info;

    public Event(long eventTime, int cycles, String type, Object[] info) {
        this.eventTime = eventTime;
        this.cycles = cycles;
        this.type = type;
        this.info = info;
    }
}
