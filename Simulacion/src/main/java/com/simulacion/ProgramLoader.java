package com.simulacion;

import java.io.*;

public class ProgramLoader {
    private MemoryManager memoryManager;

    public ProgramLoader(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    /**
     * Takes the file and loads the bytes in memory
     * @param path path to the file that is going to be run
     * @throws IOException if there's no file for the path given
     * @return a BitSet that represents the initialAddress for execution
     */
    public BitsSet loadProgram(String path) throws IOException {
        FileInputStream binaryFile = new FileInputStream(path);
        ObjectInputStream is = new ObjectInputStream(binaryFile);

        try {
            int wordCounter = 0;
            while(true){
                BitsSet word = new BitsSet(Consts.WORD_SIZE);
                for(int i=0; i<4; i++){
                    byte tempByte = is.readByte();
                    for (int j=0; j<8; j++){
                        word.set((i*8) + j, ((tempByte >> j) & 1) == 1);
                    }
                }
                memoryManager.writeBits(Consts.STACK_SIZE + wordCounter * 4, OperandSize.Word, word);
                wordCounter++;
            }
        }
        catch (EOFException ignored){
            // End of file reached
        }

        is.close();
        binaryFile.close();

        return BitsSet.valueOf(Consts.STACK_SIZE+ Consts.DATA_SEGMENT_SIZE);
    }
}
