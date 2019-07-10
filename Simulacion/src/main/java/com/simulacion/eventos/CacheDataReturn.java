package com.simulacion.eventos;

/**
 * When the cache returned the data it was requested.
 * 0 -> BitsSet: Data returned.
 * 1 -> int: 0 Cache1 return to ControlUnit
 *           1 Cache2 return to cache 1
 *           2 Cache3 return to cache 2
 * 2 -> BitsSet: address
 */
public class CacheDataReturn extends Event {
    public CacheDataReturn(int cycles, Object[] info) {
        super(cycles, "Cache data return", info);
    }
}
