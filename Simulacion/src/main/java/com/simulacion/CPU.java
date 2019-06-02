package com.simulacion;

import com.simulacion.eventos.Halt;
import com.simulacion.eventos.StartCUCycle;

import java.util.BitSet;

public class CPU {
    private BitSet[] registers;
    private ALU alu;
    private Cache dataCache;
    private Cache instCache;
    private CPUInterconnection internalBus;
    private ControlUnit controlUnit;
    private EventHandler eventHandler;
    private long clock;
    private RxBus eventsBus;
    private boolean run;

    CPU(Cache L1DataCache, Cache L1InstCache){
        this.run = true;
        this.registers = new BitSet[32];
        this.alu = new ALU();
        this.dataCache = L1DataCache;
        this.instCache = L1InstCache;
        this.controlUnit = new ControlUnit();
        this.eventHandler = EventHandler.getInstance();
        this.clock = 0;
        this.internalBus = new CPUInterconnection(this.registers, this.alu, this.controlUnit, this.dataCache, this.instCache);
        this.controlUnit.setInternalBus(this.internalBus);
        this.eventsBus = RxBus.getInstance();
    }

    public void run(BitSet initialInstAddress){
        // Subscribe to when the program halts
        eventsBus.register(Halt.class, event -> this.run = false);
        this.controlUnit.setProgramCounter(initialInstAddress);
        eventHandler.addEvent(new StartCUCycle(0, 1, null));

        this.run = true;
        while(this.run){
            eventHandler.fireEvent(this.clock);
            ++clock;
        }
    }

}
