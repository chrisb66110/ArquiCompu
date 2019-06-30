package com.simulacion;

import com.simulacion.eventos.Halt;
import com.simulacion.eventos.SyscallExecuted;
import com.simulacion.eventos.SyscallRun;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OperatingSystem {
    private MemoryManager memory;
    private CPU cpu;
    private EventHandler eventHandler = EventHandler.getInstance();
    private Input input;
    private Output output;
    private ProgramLoader programLoader;
    private RxBus bus = RxBus.getInstance();

    public OperatingSystem() {
        ComputerBuilder.buildComputer(this);
        //Subscribes to when a syscall is requested
        bus.register(SyscallRun.class, event -> {
            Syscalls syscall = Syscalls.values()[((BitsSet) event.info[0]).toInt() - 1];
            BitsSet[] syscallInfo = (BitsSet[]) event.info[1];
            syscall(syscall, syscallInfo);
        });
    }

    public void menu(){
        boolean run = true;
        while (run){
            System.out.print("\nBienvenido a la simulación. Digite el número de la opción que desea:\n" +
                    "\t1: Cargar un programa\n" +
                    "\t2: Salir\n\n");
            Scanner reader = new Scanner(System.in);
            try {
                int option = reader.nextInt();
                reader.nextLine();
                switch (option) {
                    case 1:
                        System.out.println("Digite la dirección completa del archivo que desea correr");
                        String path = reader.nextLine();
                        this.cpu.run(this.programLoader.loadProgram(path));
                        break;
                    case 2:
                        run = false;
                        break;
                    default:
                        System.out.println("Por favor digite un número de las opciones");
                }
            }
            catch (InputMismatchException e){
                System.out.println("Por favor digite un número");
            }
            catch(IOException e){
                System.out.println("No se encontró el archivo");
            }
        }
    }

    /**
     * Executes the syscall
     * @param syscallID the syscall to be executed
     * @param syscallParams the parameters for the syscall
     */
    public void syscall(Syscalls syscallID, BitsSet[] syscallParams){
        switch(syscallID){
            case Halt:
                eventHandler.addEvent(new Halt(1, null));
                break;
            case Print:
                output.printString(syscallParams[0]);
                eventHandler.addEvent(new SyscallExecuted(5, null));
                break;
            case ReadChar:
                cpu.saveSyscallData(input.getChar());
                eventHandler.addEvent(new SyscallExecuted(4, null));
                break;
            case PrintNumber:
                output.printNumber(syscallParams[0]);
                eventHandler.addEvent(new SyscallExecuted(5, null));
                break;
            case ReadNumber:
                cpu.saveSyscallData(input.getInteger());
                eventHandler.addEvent(new SyscallExecuted(4, null));
                break;
        }
    }

    public void setMemory(MemoryManager memory) {
        this.memory = memory;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public void setProgramLoader(ProgramLoader programLoader){
        this.programLoader = programLoader;
    }
}
