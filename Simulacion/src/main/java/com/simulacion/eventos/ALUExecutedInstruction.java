package com.simulacion.eventos;

/**
 * Event to indicate that the ALU has already executed the instruction.
 * info is null, do not need to return anything.
 */
public class ALUExecutedInstruction extends Event{
    public ALUExecutedInstruction(int cycles, Object[] info) {
        super(cycles, "Operacion de ALU ejecutada", info);
    }
}
