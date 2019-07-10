package com.simulacion;

public class MemoryManager {
    private Memory memory;

    public MemoryManager(Memory memory) {
        this.memory = memory;
    }

    public BitsSet getBits(int dir, OperandSize ammount){
        return this.memory.getBits(dir, ammount);
    }

    public void writeBits(int dir, OperandSize ammount, BitsSet bits){
        this.memory.writeBits(dir, ammount, bits);
    }
}
