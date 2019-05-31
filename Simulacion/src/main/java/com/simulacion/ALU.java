package com.simulacion;

import java.util.BitSet;

public class ALU {
    public BitSet OperandA;
    public BitSet OperandB;
    public BitSet result;

    public BitSet executeOperation(ALUOperations operation){
        switch(operation){
            case Add:
                this.operationAdd();
                break;
            case Addi:
                this.operationAdd();
                break;
            case Sub:
                this.operationSub();
                break;
            case Subi:
                this.operationSub();
                break;
            case Mul:
                this.operationMul();
                break;
            case Muli:
                this.operationMul();
                break;
            case Div:
                this.operationDiv();
                break;
            case Divi:
                this.operationDiv();
                break;
            case Mod:
                this.operationMod();
                break;
            case Modi:
                this.operationMod();
                break;
            case And:
                this.operationAnd();
                break;
            case Andi:
                this.operationAnd();
                break;
            case Or:
                this.operationOr();
                break;
            case Ori:
                this.operationOr();
                break;
            case Xor:
                this.operationXor();
                break;
            case Xori:
                this.operationXor();
                break;
            case Not:
                this.operationNot();
                break;
            case Noti:
                this.operationNot();
                break;
            case Sal:
                this.operationSal();
                break;
            case Sali:
                this.operationSal();
            case Sar:
                this.operationSar();
                break;
            case Sari:
                this.operationSar();
                break;
            case Sll:
                this.operationSll();
                break;
            case Slli:
                this.operationSll();
                break;
            case Slr:
                this.operationSlr();
                break;
            case Slri:
                this.operationSlr();
                break;
            case Scl:
                this.operationScl();
                break;
            case Scli:
                this.operationScl();
                break;
            case Scr:
                this.operationScr();
                break;
            case Scri:
                this.operationScr();
                break;
            case Err:

        }
        return null;
    }

    public void operationAdd(){

    }

    public void operationSub(){

    }

    public void operationMul(){

    }

    public void operationDiv(){

    }

    public void operationMod(){

    }

    public void operationAnd(){
        this.result = this.OperandA;
        this.result.and(this.OperandB);
    }

    public void operationOr(){
        this.result = this.OperandA;
        this.result.or(this.OperandB);
    }

    public void operationXor(){
        this.result = this.OperandA;
        this.result.xor(this.OperandB);
    }

    public void operationNot(){
        this.result = this.OperandA;
        this.result.flip(0, this.OperandA.length());
    }

    public void operationSal(){
        this.result = this.OperandA;
        // Pos 0 porque el shift no deberia ser un digito mayor a 32
        byte shift = this.OperandB.toByteArray()[0];
        byte finalShift = (byte)(this.result.length()-shift);
        int i;
        for (i=0; i<finalShift; i++)
            this.result.set(i, this.result.get(i+shift));
        for (i = i; i<this.result.length(); i++)
            this.result.set(i, true);
    }

    public void operationSar(){

    }

    public void operationSll(){

    }

    public void operationSlr(){

    }

    public void operationScl(){

    }

    public void operationScr(){

    }

    /*public static void main(String[] args) {
        BitSet bitSet = new BitSet(31);
        bitSet.set(0, 31,true);
        System.out.println(bitSet.toString());

        BitSet valor = new BitSet(31);
        valor.set(0, 7,true);
        System.out.println(valor.toString());

        valor.flip(0,31);
        System.out.println(valor.length());
    }*/
}
