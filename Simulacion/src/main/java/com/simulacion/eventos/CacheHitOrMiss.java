package com.simulacion.eventos;

/**
 * Triggers when the cache finishes looking for the data it was asked to look for
 * No info
 */
public class CacheHitOrMiss extends Event {
    public CacheHitOrMiss(int cycles, Object[] info) {
        super(cycles, "Cache hit or miss", info);
    }
}
