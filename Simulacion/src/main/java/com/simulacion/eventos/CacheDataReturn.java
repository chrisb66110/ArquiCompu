package com.simulacion.eventos;

/**
 * When the cache returned the data it was requested.
 * 0 -> BitsSet: Data returned.
 */
public class CacheDataReturn extends Event {
    public CacheDataReturn(int cycles, Object[] info) {
        super(cycles, "Cache data return", info);
    }
}
