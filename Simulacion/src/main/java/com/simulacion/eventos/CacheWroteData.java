package com.simulacion.eventos;

/**
 * When the cache wrote the data
 */
public class CacheWroteData extends Event {
    public CacheWroteData(int cycles, Object[] info) {
        super(cycles, "Cache retorna datos", info);
    }
}
