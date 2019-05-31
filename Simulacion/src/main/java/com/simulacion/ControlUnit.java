package com.simulacion;

import com.simulacion.eventos.CacheTraeMemoria;

import java.util.BitSet;

import static com.simulacion.ALUOperands.OperandA;

public class ControlUnit {
    private BitSet programCounter;
    private BitSet instructionRegister;
    private CPUInterconnection internalBus;
    private OperatingSystem os;
    private EventsHandler eventHandler;

    private RxBus bus = RxBus.getInstance();

    public void fetchNextInstruction(){
        this.internalBus.loadInstructionToIR(this.programCounter);
        /**
         * Creo que aqui el evento deberia ser como CacheDevuelveInstruccion
         */
        this.bus.register(CacheTraeMemoria.class, evento -> {
            instructionRegister = (BitSet) evento.info[0];
            this.decodeInstruction();
        });
        /**
         * Preguntar donde irial el codigo que trigerea el evento
         */
    }

    public void decodeInstruction(){
        ALUOperations operation = this.bitSetToALUOperations(this.instructionRegister.get(0,6));
        this.executeInstruciton();
    }

    public void controlUnitCycle(){
        this.fetchNextInstruction();
    }

    /**
     * Función que convierte de BitSet a ALUOperations.
     * @param instruction BitSet de un byte donde está la instrucción.
     * @return ALUOperations operación que decodificada.
     */
    private ALUOperations bitSetToALUOperations(BitSet instruction){
        //Operacion a ejecutar
        ALUOperations operation;
        //La comparacion se hace con el numero decimal de la combinacion de bits de cada instruccion
        //En este caso serian los ultimos bits del BitSet
        switch(instruction.get(0,6).toByteArray()[0]){
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
                this.operationRegisterMemory(operation, instruction, OperandSize.Byte, true);
                break;
            case 33:
                operation = ALUOperations.Lub;
                this.operationRegisterMemory(operation, instruction, OperandSize.Byte, false);
                break;
            case 34:
                operation = ALUOperations.Lsh;
                this.operationRegisterMemory(operation, instruction, OperandSize.HalfWord, true);
                break;
            case 35:
                operation = ALUOperations.Luh;
                this.operationRegisterMemory(operation, instruction, OperandSize.HalfWord, false);
                break;
            case 36:
                operation = ALUOperations.Lsw;
                this.operationRegisterMemory(operation, instruction, OperandSize.Word, true);
                break;
            case 37:
                operation = ALUOperations.Luw;
                this.operationRegisterMemory(operation, instruction, OperandSize.Word, false);
                break;
            case 38:
                operation = ALUOperations.Sb;
                this.operationMemoryRegister(operation, instruction, OperandSize.Byte);
                break;
            case 39:
                operation = ALUOperations.Sh;
                this.operationMemoryRegister(operation, instruction, OperandSize.HalfWord);
                break;
            case 40:
                operation = ALUOperations.Sw;
                this.operationMemoryRegister(operation, instruction, OperandSize.Word);
                break;
            case 41:
                operation = ALUOperations.Jmp;
                this.programCounter = instruction.get(6,22);
                break;
            case 42:
                operation = ALUOperations.Je;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 43:
                operation = ALUOperations.Jne;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 44:
                operation = ALUOperations.Jg;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 45:
                operation = ALUOperations.Jges;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 46:
                operation = ALUOperations.Jgeu;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 47:
                operation = ALUOperations.Jls;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 48:
                operation = ALUOperations.Jlu;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 49:
                operation = ALUOperations.Jles;
                this.operationMemoryRegisterRegister(operation, instruction);
                break;
            case 50:
                operation = ALUOperations.Jleu;
                this.operationMemoryRegisterRegister(operation, instruction);
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
                /**
                 * FALTA ver como se va a manejar esta excepcion
                 */
        }
        return operation;
    }

    private void operationRegisterRegisterRegister(ALUOperations operation, BitSet instruction){
        byte registerResult = instruction.get(6,11).toByteArray()[0];
        byte registerA = instruction.get(11,16).toByteArray()[0];
        byte registerB = instruction.get(16,21).toByteArray()[0];

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        /**
         * FALTA mandar a ejecutar la instruccion
        */
         this.internalBus.saveALUResultToRegister(registerResult);
    }

    private void operationRegisterRegisterInmediate(ALUOperations operation, BitSet instruction){
        byte registerResult = instruction.get(6,11).toByteArray()[0];
        byte registerA = instruction.get(11,16).toByteArray()[0];
        BitSet inmmediate = instruction.get(16,32);

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandB);
        /**
         * FALTA mandar a ejecutar la instruccion
         */
        this.internalBus.saveALUResultToRegister(registerResult);
    }

    private void operationRegisterRegister(ALUOperations operation, BitSet instruction){
        byte registerResult = instruction.get(6,11).toByteArray()[0];
        byte registerA = instruction.get(11,16).toByteArray()[0];

        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        /**
         * FALTA mandar a ejecutar la instruccion
         */
        this.internalBus.saveALUResultToRegister(registerResult);
    }

    private void operationRegisterInmediate(ALUOperations operation, BitSet instruction){
        byte registerResult = instruction.get(6,11).toByteArray()[0];
        BitSet inmmediate = instruction.get(11,27);

        this.internalBus.loadImmediateToALU(inmmediate, ALUOperands.OperandA);
        /**
         * FALTA mandar a ejecutar la instruccion
         */
        this.internalBus.saveALUResultToRegister(registerResult);
    }

    private void operationRegisterMemory(ALUOperations operation, BitSet instruction, OperandSize operandSize, boolean signed){
        byte registerResult = instruction.get(6,11).toByteArray()[0];
        byte offset = instruction.get(11,27).toByteArray()[0];

        /**
         * Falta mandar a cargar ese offset
         * FALTA mandar a ejecutar la instruccion
         */
        this.internalBus.loadMemoryToRegister(registerResult, offset, operandSize, signed);
    }

    private void operationMemoryRegister(ALUOperations operation, BitSet instruction, OperandSize operandSize){
        byte offset = instruction.get(6,22).toByteArray()[0];
        byte register = instruction.get(22,27).toByteArray()[0];
        /**
         * Falta mandar a cargar ese offset
         * FALTA mandar a ejecutar la instruccion
         */
        this.internalBus.storeRegisterToMemory(register,offset, operandSize);
    }



    private void operationMemoryRegisterRegister(ALUOperations operation, BitSet instruction){
        BitSet offset = instruction.get(6,22);
        byte registerA = instruction.get(22,27).toByteArray()[0];
        byte registerB = instruction.get(27,32).toByteArray()[0];
        this.internalBus.loadRegisterToALU(registerA, ALUOperands.OperandA);
        this.internalBus.loadRegisterToALU(registerB, ALUOperands.OperandB);
        /**
         * FALTA mandar a ejecutar la instruccion
         */
        BitSet result = this.internalBus.getALUResult();
        if (result.toByteArray()[0] == 1){
            this.programCounter = offset;
        }
    }

    private void operationCall(ALUOperations operation, BitSet instruction){
        BitSet offset = instruction.get(6,22);
        /**
         * FALTA guardar la direccion de retorno en la pila
         * this.saveStack();
         */
        this.programCounter = offset;
    }

    private void operationRet(ALUOperations operation, BitSet instruction){
        BitSet offset = instruction.get(6,22);
        /**
         * FALTA sacar la direccion de retorno en la pila
         * this.programCounter offset = this.popStack();
         */
        this.programCounter = offset;
    }

    private void operationSysCall(ALUOperations operation, BitSet instruction){
        /**
         * FALTA revisar como es eso de R3+ se toman como parametros
         * Hablar de como se va a ejecutar esto
         */
    }

    public void executeInstruciton(){
        /**
         * Falta un metodo en CPUInterconnection para mandar a ejecutar la vara
         */
    }

    public BitSet instruction(){
        return null;
    }

    /*public static void main(String[] args) {
        BitSet bitSet = new BitSet(8);
        bitSet.set(0,true);
        bitSet.set(1,false);
        bitSet.set(2,true);
        bitSet.set(3,false);
        bitSet.set(4,true);
        bitSet.set(5,false);
        bitSet.set(6,true);
        bitSet.set(7,true);

        ControlUnit controlUnit = new ControlUnit();

        System.out.println(controlUnit.decodeInstruction(bitSet));
    }*/

}
