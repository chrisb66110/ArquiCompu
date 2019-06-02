package com.simulacion;

import java.util.BitSet;

public class CPUInterconnection {
    private BitSet [] registers;
    private ALU alu;
    private ControlUnit controlUnit;
    private Cache dataCache;
    private Cache instCache;

    CPUInterconnection(BitSet[] registers, ALU alu, ControlUnit controlUnit, Cache dataCache, Cache instCache){
        this.registers = registers;
        this.alu = alu;
        this.controlUnit = controlUnit;
        this.dataCache = dataCache;
        this.instCache = instCache;
    }

    public void loadRegisterToALU(int register, ALUOperands aluOperand){

    }

    public void loadImmediateToALU(BitSet immediate, ALUOperands aluOperand){

    }

    public void saveALUResultToRegister(int register){

    }

    public BitSet getALUResult(){
        return null;
    }

    public void loadImmediateToRegister(BitSet Immediate, int register){

    }

    public void loadMemoryToRegister(int register, int offset, OperandSize ammount, boolean signed){

    }

    public void storeRegisterToMemory(int register, int offset, OperandSize ammount){

    }

    public void loadInstructionToIR(BitSet address){
        this.instCache.getBits(address,OperandSize.Word);
    }
}
