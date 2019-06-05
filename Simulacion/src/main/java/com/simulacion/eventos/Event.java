package com.simulacion.eventos;

public class Event {
    public int cycles;
    public String type;
    public Object[] info;

    public Event(int cycles, String type, Object[] info) {
        this.cycles = cycles;
        this.type = type;
        this.info = info;
    }
}
