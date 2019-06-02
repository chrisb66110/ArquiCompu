package com.simulacion;

import java.util.BitSet;

public class Cache {
    private long size;
    private int associativity;
    private CacheSet[] sets;
    private int level;
    private int hitTime;
    private Cache nextCache;
    private Bus memoryBus;
    private EventHandler eventHandler = EventHandler.getInstance();

    public Cache(long size, int associativity, int level, int hitTime, Cache nextCache, Bus memoryBus) {
        this.size = size;
        this.associativity = associativity;
        this.level = level;
        this.hitTime = hitTime;
        this.nextCache = nextCache;
        this.memoryBus = memoryBus;
    }

    public BitSet[] getBits(BitSet address, OperandSize ammount){
        return null;
    }

    public void writeBits(BitSet address, OperandSize ammount, BitSet [] data){

    }
}
