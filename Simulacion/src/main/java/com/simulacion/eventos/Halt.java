package com.simulacion.eventos;

public class Halt extends Event {
    public Halt(long eventTime, int cycles, Object[] info) {
        super(eventTime, cycles, "Halt", info);
    }
}
