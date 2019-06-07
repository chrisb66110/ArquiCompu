package com.simulacion;

import com.simulacion.eventos.*;
import rx.Subscription;

/**
 * Clase para emular la unidad de control
 */
public class ControlUnit {
    private BitsSet programCounter; //Puntero a la siguiente instruccion
    private BitsSet instructionRegister; //Instruccion a ejecutar
    private CPUInterconnection internalBus; //Interconecion interna del CPU
    private EventHandler eventHandler = EventHandler.getInstance(); //Manejador de eventos
    private RxBus bus = RxBus.getInstance(); //RXBus para los eventos

    //Eventos utilizados en la clase
    private Subscription startCUCycle; //Subscripcion al evento StartCUCycle
    private Subscription cacheDataReturn; //Subscripcion al evento CacheBringsMemory
    private Subscription aluExecutedInstruction; //Subscipcion al evento ALUExecutedInstruction

    /**
     * Constructor de la unidad de control
     */
    public ControlUnit() {
        this.programCounter = new BitsSet(Consts.REGISTER_SIZE);
        this.instructionRegister = new BitsSet(Consts.REGISTER_SIZE);
        //Este subscribe funciona como el cichlo porque se triggerea con el evento StartCUCycle
        this.startCUCycle = bus.register(StartCUCycle.class, event -> {
            this.fetchNextInstruction();
        });
    }

    /**
     * Metodo para hacer el fetch de la instruccion y manda a decodificar
     */
    public void fetchNextInstruction(){
        this.internalBus.loadInstructionToIR(this.programCounter);
        //Este subscribe queda esperando un evento CacheDataReturn, asi sabe cuando ya esta el dato disponible
        cacheDataReturn = bus.register(CacheDataReturn.class, evento -> {
            // Guarda la instruccion retornada de cache
            instructionRegister = (BitsSet) evento.info[0];
            //Manda a decodificar y ejecutar la instruccion
            this.decodeInstruction();
            cacheDataReturn.unsubscribe();
        });
    }

    /**
     * Metodo para decodificar la instruccion y manda a ejecutar
     */
    public void decodeInstruction(){
        //Valor entero de la instruccion, son los primeros 6 bits
        int aluOperationsNumber = this.instructionRegister.get(0,6).toInt();
        //Operacion a ejecutar
        ALUOperations operation;
        //Switch de la instruccion
        switch(aluOperationsNumber){
            case 0:
                operation = ALUOperations.Add;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 1:
                operation = ALUOperations.Addi;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 2:
                operation = ALUOperations.Sub;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 3:
                operation = ALUOperations.Subi;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 4:
                operation = ALUOperations.Mul;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 5:
                operation = ALUOperations.Muli;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 6:
                operation = ALUOperations.Div;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 7:
                operation = ALUOperations.Divi;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 8:
                operation = ALUOperations.Mod;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 9:
                operation = ALUOperations.Modi;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 10:
                operation = ALUOperations.And;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 11:
                operation = ALUOperations.Andi;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 12:
                operation = ALUOperations.Or;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 13:
                operation = ALUOperations.Ori;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 14:
                operation = ALUOperations.Xor;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 15:
                operation = ALUOperations.Xori;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 16:
                operation = ALUOperations.Not;
                this.operationRegisterRegister(operation, this.instructionRegister);
                break;
            case 17:
                operation = ALUOperations.Noti;
                this.operationRegisterInmediate(operation, this.instructionRegister);
                break;
            case 18:
                operation = ALUOperations.Sal;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 19:
                operation = ALUOperations.Sali;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 20:
                operation = ALUOperations.Sar;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 21:
                operation = ALUOperations.Sari;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 22:
                operation = ALUOperations.Sll;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 23:
                operation = ALUOperations.Slli;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 24:
                operation = ALUOperations.Slr;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 25:
                operation = ALUOperations.Slri;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 26:
                operation = ALUOperations.Scl;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 27:
                operation = ALUOperations.Scli;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 28:
                operation = ALUOperations.Scr;
                this.operationRegisterRegisterRegister(operation, this.instructionRegister);
                break;
            case 29:
                operation = ALUOperations.Scri;
                this.operationRegisterRegisterInmediate(operation, this.instructionRegister);
                break;
            case 32:
                operation = ALUOperations.Lsb;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.Byte, true);
                break;
            case 33:
                operation = ALUOperations.Lub;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.Byte, false);
                break;
            case 34:
                operation = ALUOperations.Lsh;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.HalfWord, true);
                break;
            case 35:
                operation = ALUOperations.Luh;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.HalfWord, false);
                break;
            case 36:
                operation = ALUOperations.Lsw;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.Word, true);
                break;
            case 37:
                operation = ALUOperations.Luw;
                this.operationRegisterMemoryLoad(operation, this.instructionRegister, OperandSize.Word, false);
                break;
            case 38:
                operation = ALUOperations.Sb;
                this.operationRegisterMemoryStore(operation, this.instructionRegister, OperandSize.Byte);
                break;
            case 39:
                operation = ALUOperations.Sh;
                this.operationRegisterMemoryStore(operation, this.instructionRegister, OperandSize.HalfWord);
                break;
            case 40:
                operation = ALUOperations.Sw;
                this.operationRegisterMemoryStore(operation, this.instructionRegister, OperandSize.Word);
                break;
            case 41:
                operation = ALUOperations.Jmp;
                this.programCounter = this.instructionRegister.get(6,22);
                break;
            case 42:
                operation = ALUOperations.Je;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 43:
                operation = ALUOperations.Jne;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 44:
                operation = ALUOperations.Jg;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 45:
                operation = ALUOperations.Jges;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 46:
                operation = ALUOperations.Jgeu;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 47:
                operation = ALUOperations.Jls;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 48:
                operation = ALUOperations.Jlu;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 49:
                operation = ALUOperations.Jles;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 50:
                operation = ALUOperations.Jleu;
                this.operationRegisterRegisterMemory(operation, this.instructionRegister);
                break;
            case 51:
                operation = ALUOperations.Call;
                this.operationCall(operation, this.instructionRegister);
                break;
            case 53:
                operation = ALUOperations.Ret;
                this.operationRet(operation, this.instructionRegister);
                break;
            case 54:
                operation = ALUOperations.Syscall;
                this.operationSysCall(operation, this.instructionRegister);
                break;
            default:
                operation = ALUOperations.Err;
                //TODO: GENERAR EXCEPCION
        }
    }

    /**
     * Metodo para mandar a ejecutar en la ALU
     * @param operation Operacion a ejecutar
     * @param registerResult Registro donde se guardara el resultado de la ALU
     */
    private void executeInstrucitonALU(ALUOperations operation, int registerResult){
        //Se manda a ejecutar la instruccion
        this.internalBus.executeOperation(operation);
        //Este subscribe queda esperando un evento ALUExecutedInstruction, asi sabe cuando ya la alu ejecuto
        this.aluExecutedInstruction = bus.register(ALUExecutedInstruction.class, evento -> {
            //Se manda a guardar el valor en el registro indicado
            this.internalBus.saveALUResultToRegister(registerResult);
            this.aluExecutedInstruction.unsubscribe();
        });
    }

    /**
     * Metodo para ejecutar operaciones de add, sub, mul, div, mod, and, or, xor, sal, sar, sll, slr, scl, scr
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterRegister(ALUOperations operation, BitsSet instruction){
        //Se sacan operandos de la instruccion
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        int registerB = instruction.get(16,21).toInt();
        //Se cargan los valores en la ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //Se ejecuta la instruccion
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Metodo para ejecutar operaciones de addi, subi, muli, divi, modi, andi, ori, xori, sali, sari, slli, slri, scli, scri
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterInmediate(ALUOperations operation, BitsSet instruction){
        //Se sacan los operandos de la instruccion
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        BitsSet inmmediate = instruction.get(16,32);
        //Se cargan los valores a la ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandB);
        //Se manda a ejecutar las instruccion
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Metodo para ejecutar operacion not
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegister(ALUOperations operation, BitsSet instruction){
        //Se sacan los operandos de la instruccion
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        //Se carga el operando en la ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        //Se manda a ejecutar las instruccion
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Metodo para ejecutar operacion noti
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterInmediate(ALUOperations operation, BitsSet instruction){
        //Se sacan los operandos de la instruccion
        int registerResult = instruction.get(6,11).toInt();
        BitsSet inmmediate = instruction.get(11,27);
        //Se carga el operando en la ALU
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandA);
        //Se manda a ejecutar las instruccion
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Metodo para ejecutar operaciones lsb, lub. lsh, luh, lsw, luw
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     * @param signed Cargar con signo
     */
    private void operationRegisterMemoryLoad(ALUOperations operation, BitsSet instruction, OperandSize operandSize, boolean signed){
        //Se sacan los operandos de la instruccion
        int registerResult = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: ver si al offset hay que sumarle la pos inicial
        //Se manda a ejecutar las instruccion
        this.internalBus.loadMemoryToRegister(registerResult, offset, operandSize, signed);
    }

    /**
     * Metodo para ejecutar operaciones sb, sh, sw
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterMemoryStore(ALUOperations operation, BitsSet instruction, OperandSize operandSize){
        //Se sacan los operandos de la instruccion
        int register = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: ver si al offset hay que sumarle la pos inicial
        //Se manda a ejecutar la instruccion
        this.internalBus.storeRegisterToMemory(register,offset, operandSize);
    }

    /**
     * Metodo para ejecutar operaciones je, jne, jg, jges, jgeu, jls, jlu, jles, jleu
     * Este metodo se tiene que cambiar a cada uno para ver si se cambia el PC
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterMemory(ALUOperations operation, BitsSet instruction){
        //Se sacan los operandos de la instruccion
        int registerA = instruction.get(6,11).toInt();
        int registerB = instruction.get(11,16).toInt();
        BitsSet offset = instruction.get(16,32);
        // Se cargan los valores en la ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //Se manda a ejecutar la instruccion
        this.internalBus.executeOperation(operation);
        //Este subscribe queda esperando un evento ALUExecutedInstruction, asi sabe cuando ya la alu ejecuto
        this.aluExecutedInstruction = bus.register(ALUExecutedInstruction.class, evento -> {
            //Se piden los datos respuesta de la ALU
            BitsSet result = this.internalBus.getALUResult();
            //Se comprueba el resultado de la ALU a ver si fue verdadero
            //0: False
            //X !=0 : True
            if (result.toInt() != 0){
                //TODO: ver si al offset hay que sumarle la pos inicial
                this.programCounter = offset;
            }
            this.aluExecutedInstruction.unsubscribe();
        });
    }

    /**
     * Metodo para ejecutar operaciones call
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationCall(ALUOperations operation, BitsSet instruction){
        BitsSet offset = instruction.get(6,22);
        //TODO: FALTA guardar la direccion de retorno en la pila
        // this.saveStack();
        //TODO: ver si al offset hay que sumarle la pos inicial
        this.programCounter = offset;
    }

    /**
     * Metodo para ejecutar operaciones ret
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRet(ALUOperations operation, BitsSet instruction){
        BitsSet offset = instruction.get(6,22);
        //TODO:FALTA sacar la direccion de retorno en la pila
        // this.programCounter offset = this.popStack();
        //TODO: ver si al offset hay que sumarle la pos inicial
        this.programCounter = offset;
    }

    /**
     * Metodo para ejecutar operaciones syscall
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationSysCall(ALUOperations operation, BitsSet instruction){
        //TODO: FALTA revisar como es eso de R3+ se toman como parametros
        //TODO: Hablar de como se va a ejecutar esto
    }

    /**
     * Metodo cambiar el bus interno
     * @param internalBus Bus interno
     */
    public void setInternalBus(CPUInterconnection internalBus) {
        this.internalBus = internalBus;
    }

    /**
     * Metodo cambiar programCounter
     * @param programCounter Valor del programCounter
     */
    public void setProgramCounter(BitsSet programCounter) {
        this.programCounter = programCounter;
    }

}
