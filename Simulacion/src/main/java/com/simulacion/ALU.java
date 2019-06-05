package com.simulacion;

public class ALU {
    private BitsSet OperandA;
    private BitsSet OperandB;
    private BitsSet result;

    public BitsSet getResult() {
        return this.result;
    }

    public void setOperandA(BitsSet operandA) {
        this.OperandA = operandA;
    }

    public void setOperandB(BitsSet operandB) {
        this.OperandB = operandB;
    }

    public BitsSet executeOperation(ALUOperations operation){
        //TODO: Revisar si hay que quitar el retorno del BitsSet
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
                //TODO: Generar excepcion
                break;
        }
        return null;
    }

    private void operationAdd(){
        this.result = this.OperandA;
        this.result.add(this.OperandB);
    }

    private void operationSub(){
        this.result = this.OperandA;
        this.result.sub(this.OperandB);
    }

    private void operationMul(){
        this.result = this.OperandA;
        this.result.mul(this.OperandB);
    }

    private void operationDiv(){
        this.result = this.OperandA;
        this.result.div(this.OperandB);
    }

    private void operationMod(){
        this.result = this.OperandA;
        this.result.mod(this.OperandB);
    }

    private void operationAnd(){
        this.result = this.OperandA;
        this.result.and(this.OperandB);
    }

    private void operationOr(){
        this.result = this.OperandA;
        this.result.or(this.OperandB);
    }

    private void operationXor(){
        this.result = this.OperandA;
        this.result.xor(this.OperandB);
    }

    private void operationNot(){
        this.result = this.OperandA;
        this.result.flip(0, this.OperandA.length());
    }

    private void operationSal(){
        this.result = this.OperandA;
        this.result.sal(this.OperandB);
    }

    private void operationSar(){
        this.result = this.OperandA;
        this.result.sar(this.OperandB);
    }

    private void operationSll(){
        this.result = this.OperandA;
        this.result.sll(this.OperandB);
    }

    private void operationSlr(){
        this.result = this.OperandA;
        this.result.slr(this.OperandB);
    }

    private void operationScl(){
        this.result = this.OperandA;
        this.result.scl(this.OperandB);
    }

    private void operationScr(){
        this.result = this.OperandA;
        this.result.scr(this.OperandB);
    }
    // TODO: eliminar estos comentarios
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
