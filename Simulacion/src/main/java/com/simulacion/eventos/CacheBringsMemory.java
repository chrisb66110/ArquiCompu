package com.simulacion.eventos;

/**
 * When the cache obtains the data it was requested.
 * 0 -> BitSet: data returned from the memory.
 */
public class CacheBringsMemory extends Event {
    public CacheBringsMemory(int cycles, Object[] info) {
        super(cycles, "Cache brings memory", info);
    }
}
