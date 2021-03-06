package com.simulacion;

import com.simulacion.eventos.*;
import rx.Subscription;

/**
 * Class to emulate the interconnection of the CPU components.
 */
public class CPUInterconnection {
    //Const
    private final int LEVEL = 0; //Number to represent that the cache return data to the CPU.
    private final int INFO_INDEX_LEVEL = 1; //Index of the level number in the info in event.
    private final int INFO_INDEX_DATA = 0; //Index of data in the info in event.

    private BitsSet [] registers; // CPU registers.
    private ALU alu; // ALU.
    private ControlUnit controlUnit; //Control unit.
    private RxBus rXBus = RxBus.getInstance(); //Singleton by RXBus.
    private EventHandler eventHandler = EventHandler.getInstance(); //Event manager.

    //Caches
    private Cache dataCache; // Data cache.
    private Cache instCache; // Instruction cache.

    //Events used in the class
    private Subscription cacheDataReturnLoad; //Subscription to the CacheDataReturn event.
    private Subscription cacheDataReturnPop; //Subscription to the CacheDataReturn event.
    private Subscription cacheWroteDataStore; //Subscription to the CacheWroteData event.
    private Subscription cacheWroteDataPush; //Subscription to the CacheWroteData event.

    /**
     * CPUInterconection Constructor.
     * @param registers Vector registers, 32 registers.
     * @param alu ALU.
     * @param controlUnit Control unit.
     * @param dataCache Data cache.
     * @param instCache Instruction cache.
     */
    CPUInterconnection(BitsSet[] registers, ALU alu, ControlUnit controlUnit, Cache dataCache, Cache instCache){
        this.registers = registers;
        this.alu = alu;
        this.controlUnit = controlUnit;
        this.dataCache = dataCache;
        this.instCache = instCache;
    }

    /**
     * Method to load a register in the ALU.
     * @param register Number of register you want to put in the ALU.
     * @param aluOperand ALU Operand to load the register.
     */
    public void loadRegisterToALU(int register, ALUOperands aluOperand){
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(this.registers[register].get(0,32));
        }else{
            this.alu.setOperandB(this.registers[register].get(0,32));
        }
    }

    /**
     * Method to load an immediate in the ALU.
     * @param immediate Immediate value to load in the ALU.
     * @param aluOperand ALU Operand to load the immediate.
     */
    public void loadImmediateToALU(BitsSet immediate, ALUOperands aluOperand){
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(immediate);
        }else{
            this.alu.setOperandB(immediate);
        }
    }

    /**
     * Method to send the operation to the ALU.
     * @param operation Operation to execute.
     */
    public void executeOperation(ALUOperations operation){
        this.alu.executeOperation(operation);
    }

    /**
     * Method to save the result of the ALU in a register.
     * @param register Register number to save the result of the ALU.
     */
    public void saveALUResultToRegister(int register){
        this.registers[register] = this.alu.getResult();
    }

    /**
     * Method to return the result of the ALU.
     * @return Result of the ALU.
     */
    public BitsSet getALUResult(){
        return this.alu.getResult();
    }

    /**
     * Method to load a memory value in a register.
     * @param registerResult Register number to load the value.
     * @param offset Memory offset to bring the value.
     * @param ammount Size of the value to be loaded.
     * @param signed Boolean indicating whether the value read is signed or unsigned.
     */
    public void loadMemoryToRegister(ALUOperations operation, int registerResult, int registerIndex, BitsSet offset, OperandSize ammount, boolean signed){
        //Add offset and index from register
        BitsSet address = BitsSet.valueOf(offset.toInt());
        address.add(registers[registerIndex]);
        //This subscribe is waiting for a CacheDataReturn event, so you know when the available data is already available
        this.cacheDataReturnLoad = rXBus.register(CacheDataReturn.class, evento -> {
            if ((int)evento.info[this.INFO_INDEX_LEVEL] != this.LEVEL &&
                    ((BitsSet)evento.info[Consts.INFO_ADDRESS]).equals(address)){
                registers[registerResult] = (BitsSet) evento.info[this.INFO_INDEX_DATA];
                if(!signed){
                    //Case where there are that copy the sign
                    if (ammount == OperandSize.Byte) {
                        //The sign is copied from bit 7, since 8 to 32
                        //Consts.BYTE_SIZE = 8
                        registers[registerResult].set(Consts.BYTE_SIZE,
                                Consts.REGISTER_SIZE,
                                registers[registerResult].get(Consts.BYTE_SIZE-1));
                    } else if (ammount == OperandSize.HalfWord) {
                        //The sign is copied from bit 15, since 8 to 32
                        //Consts.HALFWORD_SIZE = 15
                        registers[registerResult].set(Consts.HALFWORD_SIZE,
                                Consts.REGISTER_SIZE,
                                registers[registerResult].get(Consts.HALFWORD_SIZE-1));
                    }
                }
                //Event to execute next instruction
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
                this.cacheDataReturnLoad.unsubscribe();
            }
        });
        //It is sent to bring data to the cache
        this.dataCache.getBits(address,ammount);
    }

    /**
     * Method to store the value of a register in memory.
     * @param registerResult Register number where the value is taken.
     * @param offset Offset to save the value.
     * @param ammount Size of the value to be save.
     */
    public void storeRegisterToMemory(ALUOperations operation, int registerResult, int registerIndex, BitsSet offset, OperandSize ammount){
        //This subscribe is waiting for a CacheWroteData event, so you know when the operation finished
        this.cacheWroteDataStore = rXBus.register(CacheWroteData.class, event -> {
            //Event to execute next instruction
            if(this.LEVEL == (int) event.info[Consts.INFO_LEVEL_INDEX-1]){
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
                this.cacheWroteDataStore.unsubscribe();
            }
        });
        //Add offset and index from register
        BitsSet address = BitsSet.valueOf(offset.toInt());
        address.add(registers[registerIndex]);
        //Write data in to cache
        this.dataCache.writeBits(address, ammount, this.registers[registerResult].get(0,32));
    }

    /**
     * Method to load an instruction in the IR.
     * @param address Offset to bring the instruction.
     */
    public void loadInstructionToIR(BitsSet address){
        //Get data from instruction cache
        this.instCache.getBits(address,OperandSize.Word);
    }

    /**
     * Method to save data in the stack.
     * @param address Address in stack.
     * @param programCounter Data to save in stack.
     */
    public void pushPCToStack(BitsSet address, BitsSet programCounter){
        //Write data in to stack
        this.dataCache.writeBits(address, OperandSize.Word, programCounter);
    }

    /**
     * Method to load data from in the stack
     * @param address Address to get data from the stack.
     */
    public void popStackToPC(BitsSet address){
        //Get data from stack
        this.dataCache.getBits(address,OperandSize.Word);
    }

    /**
     * Method to save data in the stack.
     * @param address Address in stack.
     * @param register Register number to save in stack.
     */
    public void pushRegisterToStack(ALUOperations operation, BitsSet address, int register){
        //The push of the complete register
        this.cacheWroteDataPush = rXBus.register(CacheWroteData.class, evento -> {
            //Event to execute next instruction
            this.eventHandler.addEvent(new StartCUCycle(operation.cycles,null));
            this.cacheWroteDataPush.unsubscribe();
        });
        //Write register in to stack
        this.dataCache.writeBits(address, OperandSize.Word, this.registers[register].get(0,32));
    }

    /**
     * Method to load data from in the stack
     * @param address Address to get data from the stack
     */
    public void popStackToRegister(ALUOperations operation, BitsSet address, int register){
        // The pop is to register complete
        this.cacheDataReturnPop = rXBus.register(CacheDataReturn.class, evento -> {
            if((int)evento.info[this.INFO_INDEX_LEVEL] == this.LEVEL &&
                    ((BitsSet)evento.info[Consts.INFO_ADDRESS]).equals(address)) {
                //Assigns the data to the register
                //info: register in the stack
                this.registers[register] = (BitsSet) evento.info[this.INFO_INDEX_DATA];
                //Event to execute next instruction
                this.eventHandler.addEvent(new StartCUCycle(operation.cycles, null));
                this.cacheDataReturnPop.unsubscribe();
            }
        });
        //Get register from stack
        this.dataCache.getBits(address,OperandSize.Word);
    }

    /**
     * Returns the data of a register
     * @param index the number of the register
     * @return the data inside the register
     */
    public BitsSet getRegisterData(int index){
        return registers[index].get(0,32);
    }
}
