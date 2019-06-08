package com.simulacion;

import com.simulacion.eventos.ALUExecutedInstruction;

/**
 * Clase para emular la ALU
 */
public class ALU {
    private BitsSet OperandA; //Primer operando de la ALU
    private BitsSet OperandB; //Segundo operando de la ALU
    private BitsSet result; //Resultado al ejecutar operacion
    private EventHandler eventHandler; //Manejador de eventos

    /**
     * Constructor que recibe el manejador de eventos
     * @param eventHandler Manejador de eventos
     */
    public ALU(EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    /**
     * Funcion para retornar el valor del resultado de la ALU
     * @return Valor del resultado de la ALU
     */
    public BitsSet getResult() {
        return this.result;
    }

    /**
     * Metodo para cambiar el valor del primer operando de la ALU
     * @param operandA Nuevo valor del primer operando
     */
    public void setOperandA(BitsSet operandA) {
        this.OperandA = operandA;
    }

    /**
     * Metodo para cambiar el valor del segundo operando de la ALU
     * @param operandB Nuevo valor del primer segundo
     */
    public void setOperandB(BitsSet operandB) {
        this.OperandB = operandB;
    }

    /**
     * Metodo para ejecutar una instruccion en la ALU
     * @param operation Operacion a ejecutar
     */
    public void executeOperation(ALUOperations operation){
        //TODO: Cargar ciclos dependiendo de la instruccion, buscar como poner valor en el enun
        int ciclos = 0;
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
            case Jg:
                this.operationJg();
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
            case Err:
                //TODO: Generar excepcion
                break;
        }
        this.eventHandler.addEvent(new ALUExecutedInstruction(ciclos,null));
    }

    /**
     * Metodo para ejecutar la operacion add
     */
    private void operationAdd(){
        this.result = this.OperandA;
        this.result.add(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion sub
     */
    private void operationSub(){
        this.result = this.OperandA;
        this.result.sub(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion mul
     */
    private void operationMul(){
        this.result = this.OperandA;
        this.result.mul(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion div
     */
    private void operationDiv(){
        this.result = this.OperandA;
        this.result.div(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion mod
     */
    private void operationMod(){
        this.result = this.OperandA;
        this.result.mod(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion and
     */
    private void operationAnd(){
        this.result = this.OperandA;
        this.result.and(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion or
     */
    private void operationOr(){
        this.result = this.OperandA;
        this.result.or(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion xor
     */
    private void operationXor(){
        this.result = this.OperandA;
        this.result.xor(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion not
     */
    private void operationNot(){
        this.result = this.OperandA;
        this.result.flip(0, this.OperandA.length());
    }

    /**
     * Metodo para ejecutar la operacion sal
     */
    private void operationSal(){
        this.result = this.OperandA;
        this.result.sal(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion sar
     */
    private void operationSar(){
        this.result = this.OperandA;
        this.result.sar(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion sll
     */
    private void operationSll(){
        this.result = this.OperandA;
        this.result.sll(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion slr
     */
    private void operationSlr(){
        this.result = this.OperandA;
        this.result.slr(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion scl
     */
    private void operationScl(){
        this.result = this.OperandA;
        this.result.scl(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion scr
     */
    private void operationScr(){
        this.result = this.OperandA;
        this.result.scr(this.OperandB);
    }

    /**
     * Metodo para ejecutar la operacion je
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
     * Metodo para ejecutar la operacion jne
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
     * Metodo para ejecutar la operacion jg
     */
    private void operationJg(){
        boolean res = this.OperandA.greater(this.OperandB);
        if(res){
            this.result = BitsSet.valueOf(1);
        }else{
            this.result = BitsSet.valueOf(0);
        }
    }

    /**
     * Metodo para ejecutar la operacion jgeu
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
     * Metodo para ejecutar la operacion jges
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
     * Metodo para ejecutar la operacion jls
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
     * Metodo para ejecutar la operacion jlu
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
     * Metodo para ejecutar la operacion jles
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
     * Metodo para ejecutar la operacion jleu
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
