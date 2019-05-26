package com.simulacion;

import java.util.BitSet;

public class CPUInterconnection {
    private BitSet [] registers;
    private ALU alu;
    private ControlUnit controlUnit;
    private Cache cache;

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

    }
}
