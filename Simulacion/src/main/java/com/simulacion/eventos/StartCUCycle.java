package com.simulacion.eventos;

/**
 * Start of the fetch-execute cycle
 * no info
 */
public class StartCUCycle extends Event {
    public StartCUCycle(int cycles, Object[] info) {
        super(cycles, "Start Control unit Cycle", info);
    }
}
