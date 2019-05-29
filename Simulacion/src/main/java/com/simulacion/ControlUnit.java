package com.simulacion;

import java.util.BitSet;

public class ControlUnit {
    private BitSet programCounter;
    private BitSet instructionRegister;
    private CPUInterconnection internalBus;
    private OperatingSystem os;
    private EventsHandler eventHandler;

    public void fetchNextInstruction(){

    }

    public void decodeInstruction(){

    }

    public ALUOperations bitSetToALUOperations(BitSet instruction){
        ALUOperations operation;
        switch(instruction.get(0,6).toByteArray()[0]){
            case 0:
                operation = ALUOperations.Add;
                break;
            case 1:
                operation = ALUOperations.Addi;
                break;
            case 2:
                operation = ALUOperations.Sub;
                break;
            case 3:
                operation = ALUOperations.Subi;
                break;
            case 4:
                operation = ALUOperations.Mul;
                break;
            case 5:
                operation = ALUOperations.Muli;
                break;
            case 6:
                operation = ALUOperations.Div;
                break;
            case 7:
                operation = ALUOperations.Divi;
                break;
            case 8:
                operation = ALUOperations.Mod;
                break;
            case 9:
                operation = ALUOperations.Modi;
                break;
            case 10:
                operation = ALUOperations.And;
                break;
            case 11:
                operation = ALUOperations.Andi;
                break;
            case 12:
                operation = ALUOperations.Or;
                break;
            case 13:
                operation = ALUOperations.Ori;
                break;
            case 14:
                operation = ALUOperations.Xor;
                break;
            case 15:
                operation = ALUOperations.Xori;
                break;
            case 16:
                operation = ALUOperations.Not;
                break;
            case 17:
                operation = ALUOperations.Noti;
                break;
            case 18:
                operation = ALUOperations.Sal;
                break;
            case 19:
                operation = ALUOperations.Sali;
                break;
            case 20:
                operation = ALUOperations.Sar;
                break;
            case 21:
                operation = ALUOperations.Sari;
                break;
            case 22:
                operation = ALUOperations.Sll;
                break;
            case 23:
                operation = ALUOperations.Slli;
                break;
            case 24:
                operation = ALUOperations.Slr;
                break;
            case 25:
                operation = ALUOperations.Slri;
                break;
            case 26:
                operation = ALUOperations.Scl;
                break;
            case 27:
                operation = ALUOperations.Scli;
                break;
            case 28:
                operation = ALUOperations.Scr;
                break;
            case 29:
                operation = ALUOperations.Scri;
                break;
            case 32:
                operation = ALUOperations.Lsb;
                break;
            case 33:
                operation = ALUOperations.Lub;
                break;
            case 34:
                operation = ALUOperations.Lsh;
                break;
            case 35:
                operation = ALUOperations.Luh;
                break;
            case 36:
                operation = ALUOperations.Lsw;
                break;
            case 37:
                operation = ALUOperations.Luw;
                break;
            case 38:
                operation = ALUOperations.Sb;
                break;
            case 39:
                operation = ALUOperations.Sh;
                break;
            case 40:
                operation = ALUOperations.Sw;
                break;
            case 41:
                operation = ALUOperations.Jmp;
                break;
            case 42:
                operation = ALUOperations.Je;
                break;
            case 43:
                operation = ALUOperations.Jne;
                break;
            case 44:
                operation = ALUOperations.Jg;
                break;
            case 45:
                operation = ALUOperations.Jges;
                break;
            case 46:
                operation = ALUOperations.Jgeu;
                break;
            case 47:
                operation = ALUOperations.Jls;
                break;
            case 48:
                operation = ALUOperations.Jlu;
                break;
            case 49:
                operation = ALUOperations.Jles;
                break;
            case 50:
                operation = ALUOperations.Jleu;
                break;
            case 51:
                operation = ALUOperations.Call;
                break;
            case 52:
                operation = ALUOperations.Call;
                break;
            case 53:
                operation = ALUOperations.Ret;
                break;
            case 54:
                operation = ALUOperations.Syscall;
                break;
            default:
                operation = ALUOperations.Err;
        }
        return operation;
    }

    public void executeInstruciton(){

    }

    public BitSet instruction(){
        return null;
    }

    /*public static void main(String[] args) {
        BitSet bitSet = new BitSet(8);
        bitSet.set(0,true);
        bitSet.set(1,false);
        bitSet.set(2,true);
        bitSet.set(3,false);
        bitSet.set(4,true);
        bitSet.set(5,false);
        bitSet.set(6,true);
        bitSet.set(7,true);

        ControlUnit controlUnit = new ControlUnit();

        System.out.println(controlUnit.decodeInstruction(bitSet));
    }*/

}
