package com.simulacion.eventos;

/**
 * When the cache wrote the data.
 * info is null, do not need to return anything.
 */
public class CacheWroteData extends Event {
    public CacheWroteData(int cycles, Object[] info) {
        super(cycles, "Cache wrote the data", info);
    }
}
