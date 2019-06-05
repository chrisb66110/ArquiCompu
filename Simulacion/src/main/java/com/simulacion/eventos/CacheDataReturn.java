package com.simulacion.eventos;

/**
 * When the cache returned the data it was requested
 * 0 -> BitSet: Data returned
 */
public class CacheDataReturn extends Event {
    public CacheDataReturn(int cycles, Object[] info) {
        super(cycles, "Cache retorna datos", info);
    }
}
