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
        //Este subscribe funciona como el ciclo porque se triggerea con el evento StartCUCycle
        this.startCUCycle = bus.register(StartCUCycle.class, event -> {
            this.fetchNextInstruction();
        });
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

    /**
     * Metodo para hacer el fetch de la instruccion y manda a decodificar
     */
    public void fetchNextInstruction(){
        //TODO: ver si puedo mover este subscribe a CPUInterconnection, hay que pasar por parametros de este constructor el IR
        //Este subscribe queda esperando un evento CacheDataReturn, asi sabe cuando ya esta el dato disponible
        cacheDataReturn = bus.register(CacheDataReturn.class, evento -> {
            // Guarda la instruccion retornada de cache
            instructionRegister = (BitsSet) evento.info[0];
            //Manda a decodificar y ejecutar la instruccion
            this.decodeInstruction();
            cacheDataReturn.unsubscribe();
        });
        //Se manda a buscar los datos
        this.internalBus.loadInstructionToIR(this.programCounter);
    }

    /**
     * Metodo para decodificar la instruccion y manda a ejecutar
     */
    public void decodeInstruction(){
        //Valor entero de la instruccion, son los primeros 6 bits
        int aluOperationsNumber = this.instructionRegister.get(0,6).toInt();
        //Switch de la instruccion
        switch(aluOperationsNumber){
            case 0:
                this.operationRegisterRegisterRegister(ALUOperations.Add, this.instructionRegister);
                break;
            case 1:
                this.operationRegisterRegisterInmediate(ALUOperations.Addi, this.instructionRegister);
                break;
            case 2:
                this.operationRegisterRegisterRegister(ALUOperations.Sub, this.instructionRegister);
                break;
            case 3:
                this.operationRegisterRegisterInmediate(ALUOperations.Subi, this.instructionRegister);
                break;
            case 4:
                this.operationRegisterRegisterRegister(ALUOperations.Mul, this.instructionRegister);
                break;
            case 5:
                this.operationRegisterRegisterInmediate(ALUOperations.Muli, this.instructionRegister);
                break;
            case 6:
                this.operationRegisterRegisterRegister(ALUOperations.Div, this.instructionRegister);
                break;
            case 7:
                this.operationRegisterRegisterInmediate(ALUOperations.Divi, this.instructionRegister);
                break;
            case 8:
                this.operationRegisterRegisterRegister(ALUOperations.Mod, this.instructionRegister);
                break;
            case 9:
                this.operationRegisterRegisterInmediate(ALUOperations.Modi, this.instructionRegister);
                break;
            case 10:
                this.operationRegisterRegisterRegister(ALUOperations.And, this.instructionRegister);
                break;
            case 11:
                this.operationRegisterRegisterInmediate(ALUOperations.Andi, this.instructionRegister);
                break;
            case 12:
                this.operationRegisterRegisterRegister(ALUOperations.Or, this.instructionRegister);
                break;
            case 13:
                this.operationRegisterRegisterInmediate(ALUOperations.Ori, this.instructionRegister);
                break;
            case 14:
                this.operationRegisterRegisterRegister(ALUOperations.Xor, this.instructionRegister);
                break;
            case 15:
                this.operationRegisterRegisterInmediate(ALUOperations.Xori, this.instructionRegister);
                break;
            case 16:
                this.operationRegisterRegister(ALUOperations.Not, this.instructionRegister);
                break;
            case 17:
                this.operationRegisterInmediate(ALUOperations.Noti, this.instructionRegister);
                break;
            case 18:
                this.operationRegisterRegisterRegister(ALUOperations.Sal, this.instructionRegister);
                break;
            case 19:
                this.operationRegisterRegisterInmediate(ALUOperations.Sali, this.instructionRegister);
                break;
            case 20:
                this.operationRegisterRegisterRegister(ALUOperations.Sar, this.instructionRegister);
                break;
            case 21:
                this.operationRegisterRegisterInmediate(ALUOperations.Sari, this.instructionRegister);
                break;
            case 22:
                this.operationRegisterRegisterRegister(ALUOperations.Sll, this.instructionRegister);
                break;
            case 23:
                this.operationRegisterRegisterInmediate(ALUOperations.Slli, this.instructionRegister);
                break;
            case 24:
                this.operationRegisterRegisterRegister(ALUOperations.Slr, this.instructionRegister);
                break;
            case 25:
                this.operationRegisterRegisterInmediate(ALUOperations.Slri, this.instructionRegister);
                break;
            case 26:
                this.operationRegisterRegisterRegister(ALUOperations.Scl, this.instructionRegister);
                break;
            case 27:
                this.operationRegisterRegisterInmediate(ALUOperations.Scli, this.instructionRegister);
                break;
            case 28:
                this.operationRegisterRegisterRegister(ALUOperations.Scr, this.instructionRegister);
                break;
            case 29:
                this.operationRegisterRegisterInmediate(ALUOperations.Scri, this.instructionRegister);
                break;
            case 32:
                this.operationRegisterMemoryLoad(ALUOperations.Lsb, this.instructionRegister, OperandSize.Byte, true);
                break;
            case 33:
                this.operationRegisterMemoryLoad(ALUOperations.Lub, this.instructionRegister, OperandSize.Byte, false);
                break;
            case 34:
                this.operationRegisterMemoryLoad(ALUOperations.Lsh, this.instructionRegister, OperandSize.HalfWord, true);
                break;
            case 35:
                this.operationRegisterMemoryLoad(ALUOperations.Luh, this.instructionRegister, OperandSize.HalfWord, false);
                break;
            case 36:
                this.operationRegisterMemoryLoad(ALUOperations.Lsw, this.instructionRegister, OperandSize.Word, true);
                break;
            case 37:
                this.operationRegisterMemoryLoad( ALUOperations.Luw, this.instructionRegister, OperandSize.Word, false);
                break;
            case 38:
                this.operationRegisterMemoryStore(ALUOperations.Sb, this.instructionRegister, OperandSize.Byte);
                break;
            case 39:
                this.operationRegisterMemoryStore(ALUOperations.Sh, this.instructionRegister, OperandSize.HalfWord);
                break;
            case 40:
                this.operationRegisterMemoryStore(ALUOperations.Sw, this.instructionRegister, OperandSize.Word);
                break;
            case 41:
                this.programCounter = this.instructionRegister.get(6,22);
                break;
            case 42:
                this.operationRegisterRegisterMemory(ALUOperations.Je, this.instructionRegister);
                break;
            case 43:
                this.operationRegisterRegisterMemory(ALUOperations.Jne, this.instructionRegister);
                break;
            case 44:
                this.operationRegisterRegisterMemory(ALUOperations.Jg, this.instructionRegister);
                break;
            case 45:
                this.operationRegisterRegisterMemory(ALUOperations.Jges, this.instructionRegister);
                break;
            case 46:
                this.operationRegisterRegisterMemory(ALUOperations.Jgeu, this.instructionRegister);
                break;
            case 47:
                this.operationRegisterRegisterMemory(ALUOperations.Jls, this.instructionRegister);
                break;
            case 48:
                this.operationRegisterRegisterMemory(ALUOperations.Jlu, this.instructionRegister);
                break;
            case 49:
                this.operationRegisterRegisterMemory(ALUOperations.Jles, this.instructionRegister);
                break;
            case 50:
                this.operationRegisterRegisterMemory(ALUOperations.Jleu, this.instructionRegister);
                break;
            case 51:
                this.operationCall(ALUOperations.Call, this.instructionRegister);
                break;
            case 53:
                this.operationRet(ALUOperations.Ret, this.instructionRegister);
                break;
            case 54:
                this.operationSysCall(ALUOperations.Syscall, this.instructionRegister);
                break;
            default:
                //TODO: GENERAR EXCEPCION
        }
    }

    /**
     * Metodo para mandar a ejecutar en la ALU
     * @param operation Operacion a ejecutar
     * @param registerResult Registro donde se guardara el resultado de la ALU
     */
    private void executeInstrucitonALU(ALUOperations operation, int registerResult){
        //TODO: ver si puedo mover este subscribe a CPUInterconnection
        //Este subscribe queda esperando un evento ALUExecutedInstruction, asi sabe cuando ya la alu ejecuto
        this.aluExecutedInstruction = bus.register(ALUExecutedInstruction.class, evento -> {
            //Se manda a guardar el valor en el registro indicado
            this.internalBus.saveALUResultToRegister(registerResult);
            this.aluExecutedInstruction.unsubscribe();
            //TODO: aqui se debe generar un evento de fin de instruccion
        });
        //Se manda a ejecutar la instruccion
        this.internalBus.executeOperation(operation);
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
        //TODO: ver si puedo mover este subscribe a CPUInterconnection, hay que pasar por parametros de este constructor el pc
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
            //TODO: aqui se debe generar un evento de fin de instruccion
        });
        //Se manda a ejecutar la instruccion
        this.internalBus.executeOperation(operation);
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
        //TODO: aqui se debe generar un evento de fin de instruccion
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
        //TODO: aqui se debe generar un evento de fin de instruccion
    }

    /**
     * Metodo para ejecutar operaciones syscall
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationSysCall(ALUOperations operation, BitsSet instruction){
        //TODO: FALTA revisar como es eso de R3+ se toman como parametros
        //TODO: Hablar de como se va a ejecutar esto
        //TODO: aqui se debe generar un evento de fin de instruccion
    }

}
