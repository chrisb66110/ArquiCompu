package com.simulacion.eventos;

/**
 * When the cache obtains the data it was requested
 * 0 -> BitSet: es el dato que se trajo de memoria
 */
public class CacheBringsMemory extends Event {
    public CacheBringsMemory(int cycles, Object[] info) {
        super(cycles, "Cache trae memoria", info);
    }
}
