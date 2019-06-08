package com.simulacion.eventos;

/**
 * Bus broadcasts the content in its lines
 * No data in object since the listeners directly get the data from the bus lines
 */
public class BusSendsSignal extends Event {
    public BusSendsSignal(int cycles, Object[] info) {
        super(cycles, "Bus sends signal", info);
    }
}
