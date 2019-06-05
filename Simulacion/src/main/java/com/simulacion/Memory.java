package com.simulacion;

public class Memory {
    private BitsSet[] memory;
    private EventHandler eventHandler = EventHandler.getInstance();
    private Bus bus;

    public Memory(int size, Bus bus) {
        this.memory = new BitsSet[size];
        this.bus = bus;
    }

    public BitsSet[] getBits(int dir, OperandSize ammount){
        return null;
    }

    public void writeBits(int dir, OperandSize ammount, BitsSet[] data){

    }
}
