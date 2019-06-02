package com.simulacion.eventos;

public class Halt extends Event {
    public Halt(int cycles, Object[] info) {
        super(cycles, "Halt", info);
    }
}
