package com.simulacion;

import java.util.BitSet;

public class Memory {
    private BitSet[] memory;
    private EventHandler eventHandler = EventHandler.getInstance();
    private Bus bus;

    public Memory(int size, Bus bus) {
        this.memory = new BitSet[size];
        this.bus = bus;
    }

    public BitSet[] getBits(int dir, OperandSize ammount){
        return null;
    }

    public void writeBits(int dir, OperandSize ammount, BitSet[] data){

    }
}
