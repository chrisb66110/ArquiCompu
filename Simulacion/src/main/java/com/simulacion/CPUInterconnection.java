package com.simulacion;

public class CPUInterconnection {
    private BitsSet [] registers;
    private ALU alu;
    private ControlUnit controlUnit;
    private Cache dataCache;
    private Cache instCache;

    CPUInterconnection(BitsSet[] registers, ALU alu, ControlUnit controlUnit, Cache dataCache, Cache instCache){
        this.registers = registers;
        this.alu = alu;
        this.controlUnit = controlUnit;
        this.dataCache = dataCache;
        this.instCache = instCache;
    }

    private RxBus bus = RxBus.getInstance();

    public CPUInterconnection(){
        this.registers = new BitsSet[32];
        for (BitsSet regiter: registers) {
            regiter = new BitsSet(32);
        }
    }

    public void loadRegisterToALU(int register, ALUOperands aluOperand){
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(this.registers[register]);
        }else{
            this.alu.setOperandB(this.registers[register]);
        }
    }

    public void loadImmediateToALU(BitsSet immediate, ALUOperands aluOperand){
        //TODO: Ver si hay que revisar que el BitSet es de 32
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(immediate);
        }else{
            this.alu.setOperandB(immediate);
        }
    }

    public void executeOperation(ALUOperations operation){
        this.alu.executeOperation(operation);
    }

    public void saveALUResultToRegister(int register){
        //TODO: Ver si hay que revisar que el BitSet no hace overflow
        this.registers[register] = this.alu.getResult();
    }

    public BitsSet getALUResult(){
        //TODO: Ver si hay que revisar que el BitSet no hace overflow
        return this.alu.getResult();
    }

    public void loadImmediateToRegister(BitsSet Immediate, int register){
        //TODO: Para que era este metodo?
    }

    public void loadMemoryToRegister(int register, BitsSet offset, OperandSize ammount, boolean signed){
        //TODO: Revisar que ese metodo genere el evento de que ya cache retorno los datos
        this.instCache.getBits(offset,ammount);
    }

    public void storeRegisterToMemory(int register, BitsSet offset, OperandSize ammount){
        //TODO: Revisar que ese metodo genere el evento de que ya cache escribio los datos
        this.instCache.writeBits(offset, ammount, this.registers[register]);
    }

    public void loadInstructionToIR(BitsSet address){
        //TODO: Revisar que ese metodo genere el evento de que ya cache retorno los datos
        this.instCache.getBits(address,OperandSize.Word);
    }
}
