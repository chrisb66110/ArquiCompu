package com.simulacion.eventos;

/**
 * Evento para indicar que la ALU ya ejecutó la instrucción
 * info es null, no necesita retornar nada
 */
public class ALUExecutedInstruction extends Event{
    public ALUExecutedInstruction(int cycles, Object[] info) {
        super(cycles, "Operacion ejecutada", info);
    }
}
