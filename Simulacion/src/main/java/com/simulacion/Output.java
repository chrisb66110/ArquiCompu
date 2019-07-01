package com.simulacion;

public class Output {
    private MemoryManager memoryManager;

    public Output(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    /**
     * Prints the characters until it finds a \0
     * @param address the address where the string begins
     */
    public void printString(BitsSet address) {
        boolean eofFound = false;
        String string = "";
        int initialAddress = address.toInt();
        while(!eofFound) {
            char character = (char) memoryManager.getBits(initialAddress, OperandSize.Byte).toInt();
            if (character != '\0')
                string = string + character;
            else
                eofFound = true;
            initialAddress ++;
        }
        System.out.print(string);
    }

    /**
     * Prints the number represented by the BitsSet
     * @param number the number to be printed
     */
    public void printNumber(BitsSet number) {
        System.out.print(number.toInt());
    }
}
