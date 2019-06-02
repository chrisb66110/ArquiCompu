package com.simulacion.eventos;

/**
 * Start of the fetch-execute cycle
 * no info
 */
public class StartCUCycle extends Event {
    public StartCUCycle(long eventTime, int cycles, Object[] info) {
        super(eventTime, cycles, "Start Control unit Cycle", info);
    }
}
