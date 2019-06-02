package com.simulacion;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OperatingSystem {
    private MemoryManager memory;
    private CPU cpu;
    private EventHandler eventHandler = EventHandler.getInstance();
    private Input input;
    private Output output;
    private ProgramLoader programLoader;

    public OperatingSystem() {
        ComputerBuilder.buildComputer(this);
    }

    public void menu(){
        boolean run = true;
        while (run){
            System.out.print("Bienvenido a la simulación. Digite el número de la opción que desea:\n" +
                    "\t1: Cargar un programa\n" +
                    "\t2: Salir\n\n");
            Scanner reader = new Scanner(System.in);
            try {
                int option = reader.nextInt();
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
            catch(FileNotFoundException e){
                System.out.println("No se encontró el archivo");
            }
        }
    }

    public void syscall(Syscalls syscallID){

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
