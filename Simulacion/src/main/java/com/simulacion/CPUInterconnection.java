package com.simulacion;

import com.simulacion.eventos.CacheTraeMemoria;

import java.util.BitSet;

public class CPUInterconnection {
    private BitSet [] registers;
    private ALU alu;
    private ControlUnit controlUnit;
    private Cache cache;

    private RxBus bus = RxBus.getInstance();

    public CPUInterconnection(){
        this.registers = new BitSet[32];
        for (BitSet regiter: registers) {
            regiter = new BitSet(32);
        }
    }

    public void loadRegisterToALU(int register, ALUOperands aluOperand){
        if (aluOperand == ALUOperands.OperandA){
            this.alu.OperandA = this.registers[register];
        }else{
            this.alu.OperandB = this.registers[register];
        }
    }

    public void loadImmediateToALU(BitSet immediate, ALUOperands aluOperand){
        /**
         * Ver si hay que revisar que el BitSet es de 32
         */
        if (aluOperand == ALUOperands.OperandA){
            this.alu.OperandA = immediate;
        }else{
            this.alu.OperandB = immediate;
        }
    }

    public void saveALUResultToRegister(int register){
        /**
         * Ver si hay que revisar que el BitSet no hace overflow
         */
        this.registers[register] = this.alu.result;
    }

    public BitSet getALUResult(){
        /**
         * Ver si hay que revisar que el BitSet no hace overflow
         */
        return this.alu.result;
    }

    public void loadImmediateToRegister(BitSet Immediate, int register){
        /**
         * Hablar de para que es este, no se en que caso se puede usar
         */
    }

    public void loadMemoryToRegister(int register, int offset, OperandSize ammount, boolean signed){
        /**
         * Hablar con los compañeros porque creo que es mejor que el offset sea un BitSet
         * Hablar de para que es ese parametro de signed? porque no hay load con esa diferencia
         */
//        this.cache.getBits(offset,ammount);
        /**
         * Revisar porque aqui creo que va otro que es que ya estan los datos en cache
         * porque puede que tenga que ir a memoria
         * en tal caso el post de abajo va dentro de ese evento
         */
//        this.bus.register(CacheDevuelveDatos.class, evento -> {
//            this.registers[register] = (BitSet) evento.info[0];
//            bus.post(new LoadMemoryToRegister());
//        });
    }

    public void storeRegisterToMemory(int register, int offset, OperandSize ammount){
        /**
         * Hablar de en que casos el tercer parametro seria un Bitset[], porque se puede cambiar a BitSet
         */
//        this.cache.writeBits(offset, ammount, this.registers[register]);
//        this.bus.register(CacheTerminoEscribir.class, evento -> {
//            bus.post(new StoreRegisterToMemory());
//        });
    }

    public void loadInstructionToIR(BitSet address){
        this.cache.getBits(address,OperandSize.Word);
        bus.post(new CacheTraeMemoria());
        /**
         * Revisar porque aqui creo que va otro que es que ya estan los datos en cache
         * porque puede que tenga que ir a memoria
         * en tal caso el post de abajo va dentro de ese evento
         * Algo como lo que esta abajo
         */
//        this.bus.register(CacheDevuelveDatos.class, evento -> {
//            this.registers[register] = (BitSet) evento.info[0];
//            bus.post(new CacheDevuelveInstruccion());
//        });

    }
}
