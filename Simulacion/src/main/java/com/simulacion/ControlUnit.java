package com.simulacion;

import com.simulacion.eventos.*;
import rx.Subscription;

/**
 * Class to emulate the control unit.
 */
public class ControlUnit {
    //TODO: hablar que actualmente el stackpointer apunta a la ultima utilizanda y que esta posicion deberia ser relativa
    private BitsSet stackPointer = BitsSet.valueOf(0);//Pointer to the last address used, it necessary sum the PC to get data
    private BitsSet programCounter; //Pointer to the next instruction.
    private BitsSet instructionRegister; //Instruction to execute.
    private CPUInterconnection internalBus; //Internal interconnection of the CPU.
    private EventHandler eventHandler = EventHandler.getInstance(); //Event manager.
    private RxBus bus = RxBus.getInstance(); //RXBus for events.

    //Events used in the class
    private Subscription startCUCycle; //Subscription to the StartCUCycle event.
    private Subscription cacheDataReturn; //Subscription to the event CacheBringsMemory event.
    private Subscription cacheWroteData; //Subscription to the event CacheWroteData
    private Subscription aluExecutedInstruction; //Subscription to the ALUExecutedInstruction event.
    private Subscription syscallExecuted; //Subscription to the SyscallRun event.

    /**
     * Control unit constructor.
     */
    public ControlUnit() {
        this.programCounter = new BitsSet(Consts.REGISTER_SIZE);
        this.instructionRegister = new BitsSet(Consts.REGISTER_SIZE);
        //This subscribe works like the cycle because it is triggered by the StartCUCycle event
        this.startCUCycle = bus.register(StartCUCycle.class, event -> {
            this.fetchNextInstruction();
        });
    }

    /**
     * Method to change the internal bus.
     * @param internalBus Internal bus.
     */
    public void setInternalBus(CPUInterconnection internalBus) {
        this.internalBus = internalBus;
    }

    /**
     * Method change programCounter.
     * @param programCounter Value of the programCounter.
     */
    public void setProgramCounter(BitsSet programCounter) {
        this.programCounter = programCounter;
    }

    /**
     * Method to do the fetch of the instruction and send to decode.
     */
    public void fetchNextInstruction(){
        //This subscribe is waiting for a CacheDataReturn event, so you know when the available data is already available
        this.cacheDataReturn = bus.register(CacheDataReturn.class, event -> {
            //Save the returned cache instruction
            instructionRegister = (BitsSet) event.info[0];
            //Command to decode and execute the instruction
            this.decodeInstruction();
            this.cacheDataReturn.unsubscribe();
        });
        //It is sent to look for the data
        this.internalBus.loadInstructionToIR(this.programCounter);
    }

    /**
     * Method to decode the instruction and send to execute.
     */
    public void decodeInstruction(){
        //Integer value of the instruction, are the first 6 bits
        int aluOperationsNumber = this.instructionRegister.get(0,6).toInt();
        //Instruction switch
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
                //Sum the offset to the PC
                BitsSet offset = this.instructionRegister.get(6,22);
                this.programCounter.add(offset);
                //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
                this.eventHandler.addEvent(new StartCUCycle(0,null));
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
                //TODO: revisar excepcion
                //throw new Exception("No se reconoce la insruccion");
        }
    }

    /**
     * Method to send to execute in the ALU.
     * @param operation Operation to execute.
     * @param registerResult Record where the result of the ALU will be kept.
     */
    private void executeInstrucitonALU(ALUOperations operation, int registerResult){
        //This subscribe is waiting for an ALUExecutedInstruction event, so it knows when the alu executes
        this.aluExecutedInstruction = bus.register(ALUExecutedInstruction.class, evento -> {
            //It is sent to save the value in the indicated register
            this.internalBus.saveALUResultToRegister(registerResult);
            this.aluExecutedInstruction.unsubscribe();
            //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
            this.eventHandler.addEvent(new StartCUCycle(0,null));
        });
        //It is sent to execute the instruction
        this.internalBus.executeOperation(operation);
    }

    /**
     * Method to execute operations of add, sub, mul, div, mod, and, or, xor, salt, sar, sll, slr, scl, scr.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegisterRegister(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        int registerB = instruction.get(16,21).toInt();
        //Values are loaded into the ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //The instruction is executed
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Method to execute operations of addi, subi, muli, divi, modi, andi, ori, xori, sali, sari, slli, slri, scli, scri.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegisterInmediate(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        BitsSet inmmediate = instruction.get(16,32);
        //The values are loaded to the ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandB);
        //It is sent to execute the instructions
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Method to execute operation not.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegister(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted
        int registerResult = instruction.get(6,11).toInt();
        int registerA = instruction.get(11,16).toInt();
        //The operand in the ALU is loaded
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        //It is sent to execute the instruction
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Method to execute operation noti.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterInmediate(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted.
        int registerResult = instruction.get(6,11).toInt();
        BitsSet inmmediate = instruction.get(11,27);
        //The operand in the ALU is loaded
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandA);
        //It is sent to execute the instruction
        this.executeInstrucitonALU(operation, registerResult);
    }

    /**
     * Method to execute operations lsb, lub. lsh, luh, lsw, luw.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     * @param signed Load signed.
     */
    private void operationRegisterMemoryLoad(ALUOperations operation, BitsSet instruction, OperandSize operandSize, boolean signed){
        //The operands of the instruction are extracted
        int registerResult = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: ver si al offset hay que sumarle la pos inicial
        //It is sent to execute the instruction
        this.internalBus.loadMemoryToRegister(registerResult, offset, operandSize, signed);
    }

    /**
     * Method to execute operations sb, sh, sw.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterMemoryStore(ALUOperations operation, BitsSet instruction, OperandSize operandSize){
        //The operands of the instruction are removed
        int register = instruction.get(6,11).toInt();
        BitsSet offset = instruction.get(11,27);
        //TODO: ver si al offset hay que sumarle la pos inicial
        //It is sent to execute the instruction
        this.internalBus.storeRegisterToMemory(register,offset, operandSize);
    }

    /**
     * Method to execute operations je, jne, jg, jges, jgeu, jl, jlu, jles, jleu.
     * This method has to be changed to each one to see if the PC is changed.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegisterMemory(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted
        int registerA = instruction.get(6,11).toInt();
        int registerB = instruction.get(11,16).toInt();
        BitsSet offset = instruction.get(16,32);
        //Values are loaded into the ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //This subscribe is waiting for an ALUExecutedInstruction event, so it knows when the alu executes
        this.aluExecutedInstruction = bus.register(ALUExecutedInstruction.class, evento -> {
            //The result is requested to the ALU
            BitsSet result = this.internalBus.getALUResult();
            //The result of the ALU is checked to see if it was true
            //0: False
            //X !=0 : True
            if (result.toInt() != 0){
                //Sum the offset to the PC
                this.programCounter.add(offset);
            }
            this.aluExecutedInstruction.unsubscribe();
            //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
            this.eventHandler.addEvent(new StartCUCycle(0,null));
        });
        //It is sent to execute the instruction
        this.internalBus.executeOperation(operation);
    }

    /**
     * Method to execute call operations.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationCall(ALUOperations operation, BitsSet instruction){
        BitsSet offset = instruction.get(6,22);
        //The push of the complete pc
        this.cacheWroteData = bus.register(CacheWroteData.class, evento -> {
            //Sum the offset to the PC
            this.programCounter.add(offset);
            //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
            this.eventHandler.addEvent(new StartCUCycle(0,null));
            this.cacheWroteData.unsubscribe();
        });
        //Sum 32 to the stackPointer, for increase the stack pointer
        this.stackPointer.add(BitsSet.valueOf(Consts.WORD_SIZE));
        //Save programCounter in the stack
        this.internalBus.pushStack(this.stackPointer, this.programCounter);
    }

    /**
     * Method to execute ret operations.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRet(ALUOperations operation, BitsSet instruction){
        // The pop is to PC complete
        this.cacheDataReturn = bus.register(CacheDataReturn.class, evento -> {
            //Assigns the pointer to the PC
            //info: programCounter in the stack
            this.programCounter = (BitsSet) evento.info[0];
            //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
            this.eventHandler.addEvent(new StartCUCycle(0,null));
            this.cacheDataReturn.unsubscribe();
        });
        //Extract programCounter to the stack
        this.internalBus.popStack(this.stackPointer);
        //Subtract 32 to the stackPointer, after because pointer the last used, for decrease the stack pointer
        this.stackPointer.sub(BitsSet.valueOf(Consts.WORD_SIZE));
    }

    /**
     * Method to execute syscall operations.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationSysCall(ALUOperations operation, BitsSet instruction){
        //TODO: FALTA revisar como es eso de R3+ se toman como parametros, revisar si hay que meter esos valores en info
        this.syscallExecuted = bus.register(SyscallExecuted.class, evento -> {
            //TODO: Aqui se genera un evento de fin de instruccion, le mando un 0
            this.eventHandler.addEvent(new StartCUCycle(0,null));
            this.syscallExecuted.unsubscribe();
        });
        //Sent to run syscall
        this.eventHandler.addEvent(new SyscallRun(0,null));
        //TODO: revisar que cuando se ejecuta un syscall, al terminar se genere el evento SyscallExecuted
    }

}
