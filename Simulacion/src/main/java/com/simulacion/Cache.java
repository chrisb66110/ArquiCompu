package com.simulacion;

import java.util.BitSet;

public class Cache {
    private CacheSet[] sets;
    private int level;
    private Cache nextCache;
    private Bus memoryBus;
    private int hitTime;
    private EventsHandler eventHandler;

    public BitSet[] getBits(BitSet address, OperandSize ammount){
        return null;
    }

    public void writeBits(BitSet address, OperandSize ammount, BitSet [] data){

    }
}
