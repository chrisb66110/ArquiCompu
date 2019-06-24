package com.simulacion;

public enum OperandSize {
    Byte (Consts.BYTE_SIZE),
    HalfWord (Consts.HALFWORD_SIZE),
    Word (Consts.WORD_SIZE); 

    public final int size;
    OperandSize(int sz) {
        this.size = sz;
    }
}
