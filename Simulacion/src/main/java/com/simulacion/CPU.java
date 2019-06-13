package com.simulacion;

import com.simulacion.eventos.Halt;
import com.simulacion.eventos.StartCUCycle;
import rx.Subscription;


public class CPU {
    private BitsSet[] registers;
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
        this.registers = new BitsSet[Consts.REGISTERS_NUMBER];
        for (BitsSet regiter: registers) {
            regiter = new BitsSet(Consts.REGISTER_SIZE);
        }
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

    public void run(BitsSet initialInstAddress){
        // Subscribe to when the program halts
        eventsBus.register(Halt.class, event -> this.run = false);
        this.controlUnit.setProgramCounter(initialInstAddress);
        eventHandler.addEvent(new StartCUCycle(1, null));

        this.run = true;
        while(this.run){
            eventHandler.fireEvent(this.clock);
            ++clock;
        }
    }

    public long getClock() {
        return clock;
    }

    /**
     * Saves the data inside the r2 register
     * @param data the data to be saved
     */
    public void saveSyscallData(BitsSet data){
        registers[2] = data;
    }
}
