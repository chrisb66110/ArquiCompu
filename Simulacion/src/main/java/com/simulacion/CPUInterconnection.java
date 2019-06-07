package com.simulacion;

import com.simulacion.eventos.CacheDataReturn;
import com.simulacion.eventos.CacheWroteData;

/**
 * Clase para emular la interconexion de los componentes de la CPU
 */
public class CPUInterconnection {
    private BitsSet [] registers; // Registros de la CPU
    private ALU alu; // ALU
    private ControlUnit controlUnit; //Unidad de control
    private Cache dataCache; // Cache de datos
    private Cache instCache; // Cache de instrucciones
    private RxBus bus = RxBus.getInstance(); //Singleton de RXBus


    /**
     * Constructor
     * @param registers Vector de registros, 32 registros
     * @param alu ALU
     * @param controlUnit Unidad de control
     * @param dataCache Cache de datos
     * @param instCache Cache de instrucciones
     */
    CPUInterconnection(BitsSet[] registers, ALU alu, ControlUnit controlUnit, Cache dataCache, Cache instCache){
        this.registers = registers;
        this.alu = alu;
        this.controlUnit = controlUnit;
        this.dataCache = dataCache;
        this.instCache = instCache;
    }

    /**
     * Metodo para cargar un registro en la ALU
     * @param register Numero de registros que quiere meter en la ALU
     * @param aluOperand Operando de la ALU donde quiere cargar el registro
     */
    public void loadRegisterToALU(int register, ALUOperands aluOperand){
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(this.registers[register]);
        }else{
            this.alu.setOperandB(this.registers[register]);
        }
    }

    /**
     * Metodo para cargar un inmediato en la ALU
     * @param immediate Valor inmediato que quiere cargar en la ALU
     * @param aluOperand Operando de la ALU donde quiere cargar el registro
     */
    public void loadImmediateToALU(BitsSet immediate, ALUOperands aluOperand){
        //TODO: Ver si hay que revisar que el BitSet es de 32
        if (aluOperand == ALUOperands.OperandA){
            this.alu.setOperandA(immediate);
        }else{
            this.alu.setOperandB(immediate);
        }
    }

    /**
     * Metodo para mandar a ejecutar la operacion a la ALU
     * @param operation Operacion que quiere ejecutar
     */
    public void executeOperation(ALUOperations operation){
        this.alu.executeOperation(operation);
    }

    /**
     * Metodo para guardar el resultado de la ALU en un registro
     * @param register Numero de registro donde se quiere guardar el resultado de la ALU
     */
    public void saveALUResultToRegister(int register){
        //TODO: Ver si hay que revisar que el BitSet no hace overflow
        this.registers[register] = this.alu.getResult();
    }

    /**
     * Metodo para obtener el resultado de la ALU
     * @return Valor actual de la ALU
     */
    public BitsSet getALUResult(){
        //TODO: Ver si hay que revisar que el BitSet no hace overflow
        return this.alu.getResult();
    }

    /**
     *
     * @param Immediate
     * @param register
     */
    public void loadImmediateToRegister(BitsSet Immediate, int register){
        //TODO: Para que era este metodo?
    }

    /**
     * Metodo para cargar un valor de memoria en un registro
     * @param register Numero de registro donde se quiere cargar el valor
     * @param offset Offset de memoria donde se quiere traer el valor
     * @param ammount Tamaño del valor que se quiere cargar
     * @param signed Booleano indicando si el valor leido es con signo o sin signo
     */
    public void loadMemoryToRegister(int register, BitsSet offset, OperandSize ammount, boolean signed){
        //TODO: Revisar que ese metodo genere el evento de que ya cache retorno los datos
        this.instCache.getBits(offset,ammount);
        //TODO: REVISAR EL FUNCIONAMIENTO DE LA SUBSCRIPCION PORQUE APENAS LA PEGUE
        //Evento de que la cache ya retorno datos
        bus.register(CacheDataReturn.class, evento -> {
            //instructionRegister = (BitsSet) evento.info[0];
            //TODO: Ver si hay que hacer algo mas
        });
    }

    /**
     * Metodo para guardar en memoria el valor de un registro
     * @param register Numero de registro donde se toma el valor
     * @param offset Offset donde se quiere guardar el valor
     * @param ammount Tamaño del valor que se quiere cargar
     */
    public void storeRegisterToMemory(int register, BitsSet offset, OperandSize ammount){
        //TODO: Revisar que ese metodo genere el evento de que ya cache escribio los datos
        this.instCache.writeBits(offset, ammount, this.registers[register]);
        //TODO: REVISAR EL FUNCIONAMIENTO DE LA SUBSCRIPCION PORQUE APENAS LA PEGUE
        //Evento de que la cache ya escribio datos
        bus.register(CacheWroteData.class, evento -> {
            //TODO: Ver que hay que hacer, OJO porque hay que ver como espera el ciclo de arriba si se hace
        });
    }

    /**
     * Metodo para cargar una instruccion en el IR
     * @param address Offset donde se quiere traer la instruccion
     */
    public void loadInstructionToIR(BitsSet address){
        //TODO: Revisar que ese metodo genere el evento de que ya cache retorno los datos
        this.instCache.getBits(address,OperandSize.Word);
    }
}
