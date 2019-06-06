package com.simulacion;

import com.simulacion.eventos.*;

public class ControlUnit {
    private BitsSet programCounter;
    private BitsSet instructionRegister;
    private CPUInterconnection internalBus;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus bus = RxBus.getInstance();

    public ControlUnit() {
        this.programCounter = new BitsSet(Consts.REGISTER_SIZE);
        this.instructionRegister = new BitsSet(Consts.REGISTER_SIZE);
        //TODO: Creo que aqui va el ciclo, contiene el siguiente evento
        bus.register(StartCUCycle.class, event -> {
            this.fetchNextInstruction();
        });
    }


    public void fetchNextInstruction(){
        //TODO: Revisar que ese metodo genere un evento CacheBringsMemory
        this.internalBus.loadInstructionToIR(this.programCounter);
        bus.register(CacheBringsMemory.class, evento -> {
            instructionRegister = (BitsSet) evento.info[0];
            this.decodeInstruction();
        });
    }

    public void decodeInstruction(){
        ALUOperations operation = this.bitSetToALUOperations(this.instructionRegister.get(0,6));
        //TODO: el executeInstruciton se mete dentro de del metodo anterior para que valla a ejecutar lo que
        // es y no tener que hacer el switch 2 veces.
        //this.executeInstruciton(operation);
    }

    /**
     * Función que convierte de BitSet a ALUOperations.
     * @param instruction BitSet de un byte donde está la instrucción.
     * @return ALUOperations operación que decodificada.
     */
    private ALUOperations bitSetToALUOperations(BitsSet instruction){
        //Operacion a ejecutar
        ALUOperations operation;
        //La comparacion se hace con el numero decimal de la combinacion de bits de cada instruccion
        //En este caso serian los ultimos bits del BitSet
        switch(instruction.get(0,6).toInt()){
            case 0:
                operation = ALUOperations.Add;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 1:
                operation = ALUOperations.Addi;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 2:
                operation = ALUOperations.Sub;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 3:
                operation = ALUOperations.Subi;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 4:
                operation = ALUOperations.Mul;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 5:
                operation = ALUOperations.Muli;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 6:
                operation = ALUOperations.Div;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 7:
                operation = ALUOperations.Divi;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 8:
                operation = ALUOperations.Mod;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 9:
                operation = ALUOperations.Modi;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 10:
                operation = ALUOperations.And;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 11:
                operation = ALUOperations.Andi;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 12:
                operation = ALUOperations.Or;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 13:
                operation = ALUOperations.Ori;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 14:
                operation = ALUOperations.Xor;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 15:
                operation = ALUOperations.Xori;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 16:
                operation = ALUOperations.Not;
                this.operationRegisterRegister(operation, instruction);
                break;
            case 17:
                operation = ALUOperations.Noti;
                this.operationRegisterInmediate(operation, instruction);
                break;
            case 18:
                operation = ALUOperations.Sal;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 19:
                operation = ALUOperations.Sali;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 20:
                operation = ALUOperations.Sar;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 21:
                operation = ALUOperations.Sari;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 22:
                operation = ALUOperations.Sll;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 23:
                operation = ALUOperations.Slli;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 24:
                operation = ALUOperations.Slr;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 25:
                operation = ALUOperations.Slri;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 26:
                operation = ALUOperations.Scl;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 27:
                operation = ALUOperations.Scli;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 28:
                operation = ALUOperations.Scr;
                this.operationRegisterRegisterRegister(operation, instruction);
                break;
            case 29:
                operation = ALUOperations.Scri;
                this.operationRegisterRegisterInmediate(operation, instruction);
                break;
            case 32:
                operation = ALUOperations.Lsb;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.Byte, true);
                break;
            case 33:
                operation = ALUOperations.Lub;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.Byte, false);
                break;
            case 34:
                operation = ALUOperations.Lsh;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.HalfWord, true);
                break;
            case 35:
                operation = ALUOperations.Luh;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.HalfWord, false);
                break;
            case 36:
                operation = ALUOperations.Lsw;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.Word, true);
                break;
            case 37:
                operation = ALUOperations.Luw;
                this.operationRegisterMemoryLoad(operation, instruction, OperandSize.Word, false);
                break;
            case 38:
                operation = ALUOperations.Sb;
                this.operationRegisterMemoryStore(operation, instruction, OperandSize.Byte);
                break;
            case 39:
                operation = ALUOperations.Sh;
                this.operationRegisterMemoryStore(operation, instruction, OperandSize.HalfWord);
                break;
            case 40:
                operation = ALUOperations.Sw;
                this.operationRegisterMemoryStore(operation, instruction, OperandSize.Word);
                break;
            case 41:
                operation = ALUOperations.Jmp;
                this.programCounter = instruction.get(6,22);
                break;
            case 42:
                operation = ALUOperations.Je;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 43:
                operation = ALUOperations.Jne;
                //Todo: en todas las operaciones de Jumps hay que hacerlas por cada
                // una, y lo que se va a hacer es quue el BitsSet tenga funciones
                // para igual, mayor, menor
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 44:
                operation = ALUOperations.Jg;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 45:
                operation = ALUOperations.Jges;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 46:
                operation = ALUOperations.Jgeu;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 47:
                operation = ALUOperations.Jls;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 48:
                operation = ALUOperations.Jlu;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 49:
                operation = ALUOperations.Jles;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 50:
                operation = ALUOperations.Jleu;
                this.operationRegisterRegisterMemory(operation, instruction);
                break;
            case 51:
                operation = ALUOperations.Call;
                this.operationCall(operation, instruction);
                break;
            case 53:
                operation = ALUOperations.Ret;
                this.operationRet(operation, instruction);
                break;
            case 54:
                operation = ALUOperations.Syscall;
                this.operationSysCall(operation, instruction);
                break;
            default:
                operation = ALUOperations.Err;
                //TODO: GENERAR EXCEPCION
        }
        return operation;
    }

    /**
     * Metodo para ejecutar operaciones de add, sub, mul, div, mod, and, or, xor, sal, sar, sll, slr, scl, scr
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterRegister(ALUOperations operation, BitsSet instruction){
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        int registerB = instruction.get(16,21).toInt();

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        this.executeInstruciton(operation);
        //Evento de que la operacion ya se ejecuto
        bus.register(ALUExecutedInstruction.class, evento -> {
            this.internalBus.saveALUResultToRegister(registerResult);
        });
    }

    /**
     * Metodo para ejecutar operaciones de addi, subi, muli, divi, modi, andi, ori, xori, sali, sari, slli, slri, scli, scri
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterInmediate(ALUOperations operation, BitsSet instruction){
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        BitsSet inmmediate = instruction.get(16,32);

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandB);
        this.executeInstruciton(operation);
        //Evento de que la operacion ya se ejecuto
        bus.register(ALUExecutedInstruction.class, evento -> {
            this.internalBus.saveALUResultToRegister(registerResult);
        });
    }

    /**
     * Metodo para ejecutar operacion not
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegister(ALUOperations operation, BitsSet instruction){
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.executeInstruciton(operation);
        //Evento de que la operacion ya se ejecuto
        bus.register(ALUExecutedInstruction.class, evento -> {
            this.internalBus.saveALUResultToRegister(registerResult);
        });
    }

    /**
     * Metodo para ejecutar operacion noti
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterInmediate(ALUOperations operation, BitsSet instruction){
        int registerResult = instruction.get(6,11).toInt();
        BitsSet inmmediate = instruction.get(11,27);

        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandA);
        this.executeInstruciton(operation);
        //Evento de que la operacion ya se ejecuto
        bus.register(ALUExecutedInstruction.class, evento -> {
            this.internalBus.saveALUResultToRegister(registerResult);
        });
    }

    /**
     * Metodo para ejecutar operaciones lsb, lub. lsh, luh, lsw, luw
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     * @param signed Cargar con signo
     */
    private void operationRegisterMemoryLoad(ALUOperations operation, BitsSet instruction, OperandSize operandSize, boolean signed){
        int registerResult = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: El ejecutar de esta es ejecutar la siguiente funcion verdad?
        //TODO: ver si al offset hay que sumarle la pos inicial
        this.internalBus.loadMemoryToRegister(registerResult, offset, operandSize, signed);
        //Evento de que la cache ya retorno datos
        bus.register(CacheDataReturn.class, evento -> {
            instructionRegister = (BitsSet) evento.info[0];
            //TODO: Ver si hay que hacer algo mas
        });
    }

    /**
     * Metodo para ejecutar operaciones sb, sh, sw
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterMemoryStore(ALUOperations operation, BitsSet instruction, OperandSize operandSize){
        int register = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: El ejecutar de esta es ejecutar la siguiente funcion verdad?
        //TODO: ver si al offset hay que sumarle la pos inicial
        this.internalBus.storeRegisterToMemory(register,offset, operandSize);
        //Evento de que la cache ya escribio datos
        bus.register(CacheWroteData.class, evento -> {
            //TODO: Ver que hay que hacer, OJO porque hay que ver como espera el ciclo de arriba si se hace
        });
    }

    /**
     * Metodo para ejecutar operaciones je, jne, jg, jges, jgeu, jls, jlu, jles, jleu
     * Este metodo se tiene que cambiar a cada uno para ver si se cambia el PC
     * @param operation Operacion a ejecutar
     * @param instruction Bits de la instruccion
     */
    private void operationRegisterRegisterMemory(ALUOperations operation, BitsSet instruction){
        int registerA = instruction.get(6,11).toInt();
        int registerB = instruction.get(11,16).toInt();
        BitsSet offset = instruction.get(16,32);

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //TODO: FALTA mandar a ejecutar la instruccion y hacer que la ALU haga esas instrucciones
        //Evento de que la cache ya escribio datos
        bus.register(ALUExecutedInstruction.class, evento -> {
            //TODO: Ver si hay que hacer algo mas
            BitsSet result = this.internalBus.getALUResult();
            if (result.toInt() == 1){
                //TODO: ver si al offset hay que sumarle la pos inicial
                this.programCounter = offset;
            }
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
     * Metodo para mandar a ejecutar en la ALU
     * @param operation Operacion a ejecutar
     */
    public void executeInstruciton(ALUOperations operation){
        //TODO: Cambiar nombre a executeInstrucitonALU
        //TODO: Falta un metodo en CPUInterconnection para mandar a ejecutar la vara
        this.internalBus.executeOperation(operation);
    }

    public void setInternalBus(CPUInterconnection internalBus) {
        this.internalBus = internalBus;
    }

    public void setProgramCounter(BitsSet programCounter) {
        this.programCounter = programCounter;
    }

}
