package com.simulacion;

/**
 * Enum to represent operations.
 */
public enum ALUOperations {
    Add (4),
    Addi (4),
    Sub (4),
    Subi (4),
    Mul (8),
    Muli (8),
    Div (10),
    Divi (10),
    Mod (10),
    Modi (10),
    And (2),
    Andi (2),
    Or (2),
    Ori (2),
    Xor (2),
    Xori (2),
    Not (2),
    Noti (2),
    Sal (4),
    Sali (4),
    Sar (4),
    Sari (4),
    Sll (4),
    Slli (4),
    Slr (4),
    Slri (4),
    Scl (4),
    Scli (4),
    Scr (4),
    Scri (4),
    Lsb (2),
    Lub (2),
    Lsh (2),
    Luh (2),
    Lsw (2),
    Luw (2),
    Sb (2),
    Sh (2),
    Sw (2),
    Jmp (2),
    Je (4),
    Jne (4),
    Jgs (4),
    Jgu (4),
    Jges (4),
    Jgeu (4),
    Jls (4),
    Jlu (4),
    Jles (4),
    Jleu (4),
    Call (4),
    Ret (4),
    Syscall (4),
    Push (2),
    Pop (2);

    public final int cycles; //Instruction cycles

    /**
     * ALUOperations constructor.
     * @param cycles Instruction cycles
     */
    ALUOperations(int cycles){
        this.cycles = cycles;
    }
}
