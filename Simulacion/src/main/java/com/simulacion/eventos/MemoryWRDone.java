package com.simulacion.eventos;

/**
 * Indicates that the memory finished reading or writing data
 * No info
 */
public class MemoryWRDone extends Event{
    public MemoryWRDone(int cycles, Object[] info) {
        super(cycles, "Memory write or read done", info);
    }
}
