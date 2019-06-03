package com.simulacion.eventos;

public class ALUExecutedInstruction extends Event{
    public ALUExecutedInstruction(int cycles, Object[] info) {
        super(cycles, "Operacion ejecutada", info);
    }
}
