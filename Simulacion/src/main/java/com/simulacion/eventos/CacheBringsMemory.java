package com.simulacion.eventos;

/**
 * When the cache obtains the data it was requested
 * 0 -> BitSet: es el dato que se trajo de memoria
 */
public class CacheBringsMemory extends Event {
    public CacheBringsMemory(long eventTime, int cycles, Object[] info) {
        super(eventTime, cycles, "Cache trae memoria", info);
    }
}
