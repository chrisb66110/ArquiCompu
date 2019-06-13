package com.simulacion;

import java.util.BitSet;
import java.util.Scanner;

public class Input {
    private Scanner input;

    public Input(){
        input = new Scanner(System.in);
    }

    /**
     * Reads a character
     * @return the character represented by the ascii number
     */
    public BitsSet getChar() {
        int typedChar = input.nextLine().charAt(0);
        return BitsSet.valueOf(typedChar);
    }

    /**
     * Read an integer
     * @return the int as a BitsSet
     */
    public BitsSet getInteger() {
        int typedNumber = Integer.parseInt(input.nextLine());
        return BitsSet.valueOf(typedNumber);
    }
}
