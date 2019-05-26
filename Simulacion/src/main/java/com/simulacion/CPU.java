package com.simulacion;

import java.util.BitSet;

public class CPU {
    private BitSet[] register;
    private ALU alu;
    private Cache memory;
    private CPUInterconnection internalBus;
    private ControlUnit controlUnit;
    private EventsHandler eventHandler;

    public void run(BitSet initialInstAddress){

    }

}
