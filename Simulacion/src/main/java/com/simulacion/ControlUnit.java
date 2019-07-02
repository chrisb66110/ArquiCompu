package com.simulacion;

import com.simulacion.eventos.*;
import rx.Subscription;

/**
 * Class to emulate the control unit.
 */
public class ControlUnit {
    //Const
    private final int INFO_INDEX_LEVEL = 1; //Index of the level number in the info in event.
    private final int LEVEL = 0; //Number to represent that the cache return data to the CPU.
    private final int INFO_INDEX_DATA = 0; //Index of data in the info in event.
    private final int INITIAL_STACK_POINTER = 127; //Initial stack pointer.
    private final BitsSet PUSH = BitsSet.valueOf(-4); //Number to mov the stack pointer in a push.
    private final BitsSet POP = BitsSet.valueOf(4); //Number to mov the stack pointer in a pop.
    private final BitsSet INCREASE_PC = BitsSet.valueOf(4); //Number to increase PC to next instruction.

    //Registers
    private BitsSet stackPointer = BitsSet.valueOf(this.INITIAL_STACK_POINTER); //Pointer to the last address non used
    private BitsSet programCounter; //Pointer to the next instruction.
    private BitsSet instructionRegister; //Instruction to execute.

    private CPUInterconnection internalBus; //Internal interconnection of the CPU.
    private EventHandler eventHandler = EventHandler.getInstance(); //Event manager.
    private RxBus rXBus = RxBus.getInstance(); //RXBus for events.

    //Events used in the class
    private Subscription startCUCycle; //Subscription to the StartCUCycle event.
    private Subscription cacheDataReturnFetch; //Subscription to the event CacheBringsMemory event.
    private Subscription cacheDataReturnOperationRet; //Subscription to the event CacheBringsMemory event.
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
        this.startCUCycle = rXBus.register(StartCUCycle.class, event -> {
            this.fetchNextInstruction();
        });
    }

    /**
     * Method to change the internal rXBus.
     * @param internalBus Internal rXBus.
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
        BitsSet address = BitsSet.valueOf(this.programCounter.toInt());
        //This subscribe is waiting for a CacheDataReturn event, so you know when the available data is already available
        this.cacheDataReturnFetch = rXBus.register(CacheDataReturn.class, event -> {
            if((int)event.info[this.INFO_INDEX_LEVEL] == this.LEVEL &&
                    ((BitsSet)event.info[Consts.INFO_ADDRESS]).equals(address)) {
                //Save the returned cache instruction
                instructionRegister = (BitsSet) event.info[this.INFO_INDEX_DATA];
                this.programCounter.add(this.INCREASE_PC);
                //Command to decode and execute the instruction
                this.decodeInstruction();
                this.cacheDataReturnFetch.unsubscribe();
            }
        });
        //It is sent to look for the data
        this.internalBus.loadInstructionToIR(this.programCounter);
    }

    /**
     * Method to decode the instruction and send to execute.
     */
    public void decodeInstruction(){
        //Integer value of the instruction, are the first 6 bits
        int aluOperationsNumber = this.instructionRegister.get(26,32).toInt();
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
                this.operationRegisterRegisterMemoryLoad(ALUOperations.Lsb, this.instructionRegister, OperandSize.Byte, true);
                break;
            case 33:
                this.operationRegisterRegisterMemoryLoad(ALUOperations.Lub, this.instructionRegister, OperandSize.Byte, false);
                break;
            case 34:
                this.operationRegisterRegisterMemoryLoad(ALUOperations.Lsh, this.instructionRegister, OperandSize.HalfWord, true);
                break;
            case 35:
                this.operationRegisterRegisterMemoryLoad(ALUOperations.Luh, this.instructionRegister, OperandSize.HalfWord, false);
                break;
            case 36:
                this.operationRegisterRegisterMemoryLoad(ALUOperations.Lsw, this.instructionRegister, OperandSize.Word, true);
                break;
            case 37:
                this.operationRegisterRegisterMemoryLoad( ALUOperations.Luw, this.instructionRegister, OperandSize.Word, false);
                break;
            case 38:
                this.operationRegisterRegisterMemoryStore(ALUOperations.Sb, this.instructionRegister, OperandSize.Byte);
                break;
            case 39:
                this.operationRegisterRegisterMemoryStore(ALUOperations.Sh, this.instructionRegister, OperandSize.HalfWord);
                break;
            case 40:
                this.operationRegisterRegisterMemoryStore(ALUOperations.Sw, this.instructionRegister, OperandSize.Word);
                break;
            case 41:
                this.operationJump(ALUOperations.Jmp, this.instructionRegister);
                break;
            case 42:
                this.operationRegisterRegisterMemory(ALUOperations.Je, this.instructionRegister);
                break;
            case 43:
                this.operationRegisterRegisterMemory(ALUOperations.Jne, this.instructionRegister);
                break;
            case 57:
                this.operationRegisterRegisterMemory(ALUOperations.Jgs, this.instructionRegister);
                break;
            case 58:
                this.operationRegisterRegisterMemory(ALUOperations.Jgu, this.instructionRegister);
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
            case 55:
                this.operationPushRegisterToStack(ALUOperations.Push, this.instructionRegister);
                break;
            case 56:
                this.operationPopRegisterToStack(ALUOperations.Pop, this.instructionRegister);
                break;
            default:
                //Sent to run syscall halt, the instruction is unknown
                System.out.println("The instruction " + aluOperationsNumber + " is unknown.");
                this.eventHandler.addEvent(new SyscallRun(1, new Object[]{BitsSet.valueOf(5)}));
                break;
        }
    }

    /**
     * Method to send to execute in the ALU.
     * @param operation Operation to execute.
     * @param registerResult Record where the result of the ALU will be kept.
     */
    private void executeInstrucitonALU(ALUOperations operation, int registerResult){
        //This subscribe is waiting for an ALUExecutedInstruction event, so it knows when the alu executes
        this.aluExecutedInstruction = rXBus.register(ALUExecutedInstruction.class, event -> {
            //It is sent to save the value in the indicated register
            this.internalBus.saveALUResultToRegister(registerResult);
            this.aluExecutedInstruction.unsubscribe();
            //Event to execute next instruction
            this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
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
        int registerResult = instruction.get(21,26).toInt();
        int registerA = instruction.get(16,21).toInt();
        int registerB = instruction.get(11,16).toInt();
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
        int registerResult = instruction.get(21,26).toInt();
        int registerA = instruction.get(16,21).toInt();
        BitsSet inmmediate = instruction.get(0,16);
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
        int registerResult = instruction.get(21,26).toInt();
        int registerA = instruction.get(16,21).toInt();
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
        int registerResult = instruction.get(21,26).toInt();
        BitsSet inmmediate = instruction.get(5,21);
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
    private void operationRegisterRegisterMemoryLoad(ALUOperations operation, BitsSet instruction, OperandSize operandSize, boolean signed){
        //The operands of the instruction are extracted
        int registerResult = instruction.get(21,26).toInt();
        int registerIndex = instruction.get(16,21).toInt();
        BitsSet offset = instruction.get(0,16);
        //It is sent to execute the instruction
        this.internalBus.loadMemoryToRegister(operation, registerResult, registerIndex, offset, operandSize, signed);
    }

    /**
     * Method to execute operations sb, sh, sw.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegisterMemoryStore(ALUOperations operation, BitsSet instruction, OperandSize operandSize){
        //The operands of the instruction are removed
        int registerResult = instruction.get(21,26).toInt();
        int registerIndex = instruction.get(16,21).toInt();
        BitsSet offset = instruction.get(0,16);
        //It is sent to execute the instruction
        this.internalBus.storeRegisterToMemory(operation, registerResult, registerIndex, offset, operandSize);
    }

    /**
     * Method to execute operation Jump.
     * @param operation Operation to execute.
     * @param instrucction Instruction bits.
     */
    private void operationJump(ALUOperations operation, BitsSet instrucction){
        //Sum the offset to the PC
        BitsSet offset = instrucction.get(10,26);
        offset.set(16,32,offset.get(15));
        //Add -4 to revert the increase the PC
        offset.add(BitsSet.valueOf(-4));
        this.programCounter.add(offset);
        //Event to execute next instruction
        this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
    }

    /**
     * Method to execute operations je, jne, jg, jges, jgeu, jl, jlu, jles, jleu.
     * This method has to be changed to each one to see if the PC is changed.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRegisterRegisterMemory(ALUOperations operation, BitsSet instruction){
        //The operands of the instruction are extracted
        int registerA = instruction.get(21,26).toInt();
        int registerB = instruction.get(16,21).toInt();
        BitsSet offset = instruction.get(0,16);
        offset.set(16,32,offset.get(15));
        //Add -4 to revert the increase the PC
        offset.add(BitsSet.valueOf(-4));
        //Values are loaded into the ALU
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        //This subscribe is waiting for an ALUExecutedInstruction event, so it knows when the alu executes
        this.aluExecutedInstruction = rXBus.register(ALUExecutedInstruction.class, evento -> {
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
            //Event to execute next instruction
            this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
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
        BitsSet offset = instruction.get(10,26);
        offset.set(16,32,offset.get(15));
        //Add -4 to revert the increase the PC
        offset.add(BitsSet.valueOf(-4));
        //The push of the complete pc
        this.cacheWroteData = rXBus.register(CacheWroteData.class, evento -> {
            if((int)evento.info[0] == this.LEVEL) {
                //Sum the offset to the PC
                this.programCounter.add(offset);
                //Event to execute next instruction
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles, null));
                this.cacheWroteData.unsubscribe();
            }
        });
        //Save programCounter in the stack
        this.internalBus.pushPCToStack(this.stackPointer, this.programCounter);
        //Sum 4 to the stackPointer, for decrease the stack pointer
        this.stackPointer.add(this.PUSH);
    }

    /**
     * Method to execute ret operations.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationRet(ALUOperations operation, BitsSet instruction){
        //Subtract 4 to the stackPointer, after because pointer the last used, for decrease the stack pointer
        this.stackPointer.add(this.POP);
        BitsSet address = BitsSet.valueOf(this.stackPointer.toInt());
        // The pop is to PC complete
        this.cacheDataReturnOperationRet = rXBus.register(CacheDataReturn.class, event -> {
            if((int)event.info[this.INFO_INDEX_LEVEL] == this.LEVEL &&
                    ((BitsSet)event.info[Consts.INFO_ADDRESS]).equals(address)) {
                //Assigns the pointer to the PC
                //info: programCounter in the stack
                this.programCounter = (BitsSet) event.info[this.INFO_INDEX_DATA];
                //Event to execute next instruction
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles, null));
                this.cacheDataReturnOperationRet.unsubscribe();
            }
        });
        //Extract programCounter to the stack
        this.internalBus.popStackToPC(this.stackPointer);
    }

    /**
     * Method to execute syscall operations.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationSysCall(ALUOperations operation, BitsSet instruction){
        if(Syscalls.values()[internalBus.getRegisterData(1).toInt() - 1] != Syscalls.Halt)
            this.syscallExecuted = rXBus.register(SyscallExecuted.class, evento -> {
                //Event to execute next instruction
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
                this.syscallExecuted.unsubscribe();
            });
        //Sent to run syscall
        this.eventHandler.addEvent(new SyscallRun(1,new Object[]{internalBus.getRegisterData(1), new BitsSet[]{internalBus.getRegisterData(3)}}));
    }

    /**
     * Method to execute push operation.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationPushRegisterToStack(ALUOperations operation, BitsSet instruction){
        //Register to save in stack
        int register = instruction.get(21,26).toInt();
        //Save register in the stack
        this.internalBus.pushRegisterToStack(operation, this.stackPointer, register);
        //Sub 4 to the stackPointer, for decrease the stack pointer
        this.stackPointer.add(this.PUSH);
    }

    /**
     * Method to execute pop operation.
     * @param operation Operation to execute.
     * @param instruction Instruction bits.
     */
    private void operationPopRegisterToStack(ALUOperations operation, BitsSet instruction){
        //Register to save in stack
        int register = instruction.get(21,26).toInt();
        //Save register in the stack
        this.internalBus.popStackToRegister(operation, this.stackPointer, register);
        //Sum 4 to the stackPointer, for increase the stack pointer
        this.stackPointer.add(this.POP);
    }

}
