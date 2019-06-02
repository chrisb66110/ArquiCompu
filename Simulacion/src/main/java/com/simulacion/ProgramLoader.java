package com.simulacion;

import java.io.FileNotFoundException;
import java.util.BitSet;

public class ProgramLoader {
    MemoryManager memoryManager;

    public ProgramLoader(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    /**
     * Takes the file and loads the bytes in memory
     * @param path path to the file that is going to be run
     * @throws FileNotFoundException if there's no file for the path given
     * @return a BitSet that represents the initialAddress for execution
     */
    public BitSet loadProgram(String path) throws FileNotFoundException {
        return null;
    }
}
