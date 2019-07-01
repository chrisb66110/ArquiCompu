package com.simulacion;

import com.simulacion.eventos.ALUExecutedInstruction;
import com.simulacion.eventos.SyscallRun;

/**
 * Class to emulate the ALU.
 */
public class ALU {
    private BitsSet OperandA; //First operator of the ALU.
    private BitsSet OperandB; //Second operator of the ALU.
    private BitsSet result; //Result when executing operation.
    private EventHandler eventHandler = EventHandler.getInstance(); //Event manager.

    /**
     * Function to return the value of the result of the ALU.
     * @return Value of the result of the ALU.
     */
    public BitsSet getResult() {
        return this.result.get(0,32);
    }

    /**
     * Method to change the value of the first operand of the ALU.
     * @param operandA New value of the first operand.
     */
    public void setOperandA(BitsSet operandA) {
        this.OperandA = operandA.get(0,32);
    }

    /**
     * Method to change the value of the second operand of the ALU.
     * @param operandB New value of the first second.
     */
    public void setOperandB(BitsSet operandB) {
        this.OperandB = operandB.get(0,32);
    }

    /**
     * Method to execute an instruction in the ALU.
     * @param operation Operation to execute.
     */
    public void executeOperation(ALUOperations operation){
        int cycles = 1;
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
            case Je:
                this.operationJe();
                break;
            case Jne:
                this.operationJne();
                break;
            case Jgs:
                this.operationJgs();
                break;
            case Jgu:
                this.operationJgu();
                break;
            case Jges:
                this.operationJges();
                break;
            case Jgeu:
                this.operationJgeu();
                break;
            case Jls:
                this.operationJls();
                break;
            case Jlu:
                this.operationJlu();
                break;
            case Jles:
                this.operationJles();
                break;
            case Jleu:
                this.operationJleu();
                break;
            default:
                //Sent to run syscall halt, the instruction is unknown
                System.out.println("The instruction " + operation + " is unknown.");
                this.eventHandler.addEvent(new SyscallRun(1, new Object[]{BitsSet.valueOf(5)}));
                break;
        }
        this.eventHandler.addEvent(new ALUExecutedInstruction(cycles,null));
    }

    /**
     * Method to execute the operation add.
     */
    private void operationAdd(){
        this.result = this.OperandA;
        this.result.add(this.OperandB);
    }

    /**
     * Method to execute the operation sub.
     */
    private void operationSub(){
        this.result = this.OperandA;
        this.result.sub(this.OperandB);
    }

    /**
     * Method to execute the operation mul.
     */
    private void operationMul(){
        this.result = this.OperandA;
        this.result.mul(this.OperandB);
    }

    /**
     * Method to execute the operation div.
     */
    private void operationDiv(){
        this.result = this.OperandA;
        this.result.div(this.OperandB);
    }

    /**
     * Method to execute the operation mod.
     */
    private void operationMod(){
        this.result = this.OperandA;
        this.result.mod(this.OperandB);
    }

    /**
     * Method to execute the operation and.
     */
    private void operationAnd(){
        this.result = this.OperandA;
        this.result.and(this.OperandB);
    }

    /**
     * Method to execute the operation or.
     */
    private void operationOr(){
        this.result = this.OperandA;
        this.result.or(this.OperandB);
    }

    /**
     * Method to execute the operation xor.
     */
    private void operationXor(){
        this.result = this.OperandA;
        this.result.xor(this.OperandB);
    }

    /**
     * Method to execute the operation not.
     */
    private void operationNot(){
        this.result = this.OperandA;
        this.result.flip(0, this.OperandA.getRealSize());
    }

    /**
     * Method to execute the operation sal.
     */
    private void operationSal(){
        this.result = this.OperandA;
        this.result.sal(this.OperandB);
    }

    /**
     * Method to execute the operation sar.
     */
    private void operationSar(){
        this.result = this.OperandA;
        this.result.sar(this.OperandB);
    }

    /**
     * Method to execute the operation sll.
     */
    private void operationSll(){
        this.result = this.OperandA;
        this.result.sll(this.OperandB);
    }

    /**
     * Method to execute the operation slr.
     */
    private void operationSlr(){
        this.result = this.OperandA;
        this.result.slr(this.OperandB);
    }

    /**
     * Method to execute the operation scl.
     */
    private void operationScl(){
        this.result = this.OperandA;
        this.result.scl(this.OperandB);
    }

    /**
     * Method to execute the operation scr.
     */
    private void operationScr(){
        this.result = this.OperandA;
        this.result.scr(this.OperandB);
    }

    /**
     * Method to execute the operation je.
     */
    private void operationJe(){
        boolean res = this.OperandA.equals(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jne.
     */
    private void operationJne(){
        boolean res = this.OperandA.notEquals(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jgs.
     */
    private void operationJgs(){
        boolean res = this.OperandA.greaterSigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jgu.
     */
    private void operationJgu(){
        boolean res = this.OperandA.greaterUnsigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jgeu.
     */
    private void operationJgeu(){
        boolean res = this.OperandA.greaterOrEqualUnsigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jges.
     */
    private void operationJges(){
        boolean res = this.OperandA.greaterOrEqualSigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jls.
     */
    private void operationJls(){
        boolean res = this.OperandA.lessSigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jlu.
     */
    private void operationJlu(){
        boolean res = this.OperandA.lessUnsigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jles.
     */
    private void operationJles(){
        boolean res = this.OperandA.lessOrEqualSigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Method to execute the operation jleu.
     */
    private void operationJleu(){
        boolean res = this.OperandA.lessOrEqualUnsigned(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

}
