package com.simulacion;

import java.util.BitSet;

/**
 * Class for handling bits.
 */
public class BitsSet{

    private BitSet bitSet; //Class to handle bits.

    /**
     * Constructor without parameters.
     */
    public BitsSet(){
        this.bitSet = new BitSet();
    }

    /**
     * Constructor from a BitSet.
     * @param bitSet BitSet to build the BitsSet.
     */
    public BitsSet(BitSet bitSet){
        this.bitSet = bitSet;
    }

    /**
     * Constructor with n bits.
     * @param nbits Amount of bits for the BitsSet.
     */
    public BitsSet(int nbits) {
        this.bitSet = new BitSet(nbits);
    }

    /**
     * Static function to build a BitsSet from a number.
     * @param number Integer to convert to BitsSet.
     * @return Returns a BitsSet object that contains the bits of the parameter number.
     */
    public static BitsSet valueOf(int number) {
        //Integer.SIZE = 32
        BitsSet newBits = new BitsSet(Integer.SIZE);
        for (int i = 0; i <Integer.SIZE; ++i) {
            newBits.set(i,(number & (1 << i)) != 0);
        }
        return newBits;
    }

    /**
     * Function that returns the value of the bitIndex position in the structure.
     * @param bitIndex Bit position.
     * @return Boolean with the value of the bitIndex position.
     */
    public boolean get(int bitIndex) {
        return this.bitSet.get(bitIndex);
    }

    /**
     * Function that returns a BitsSet with the indicated bits from fromIndex to toIndex,
     * starting at position 0 with the value of the fromIndex position.
     * @param fromIndex Position where it starts, includes the value of this position.
     * @param toIndex Next position to which it ends, that is, excludes this position.
     * @return Returns BitsSet with the indicated bits from fromIndex to toIndex.
     */
    public BitsSet get(int fromIndex, int toIndex) {
        return new BitsSet(this.bitSet.get(fromIndex, toIndex));
    }

    /**
     * Method to change the bit value of the bitIndex position.
     * @param bitIndex Position that you want to change the value.
     * @param value New value.
     */
    public void set(int bitIndex, boolean value) {
        this.bitSet.set(bitIndex, value);
    }

    /**
     * Method to change the bits values since fromIndex to toIndex.
     * @param fromIndex Position where it starts, includes the value of this position.
     * @param toIndex Next position to which it ends, that is, excludes this position.
     * @param value New value.
     */
    public void set(int fromIndex, int toIndex, boolean value) {
        this.bitSet.set(fromIndex, toIndex, value);
    }

    /**
     * Function to know the amount of bits used.
     * @return Cantidad de bits utilizados.
     */
    public int length() {
        return this.bitSet.length();
    }

    /**
     * Function that returns a copy of the object.
     * @return Return copy of the object.
     */
    public Object clone() {
        return this.bitSet.clone();
    }

    /**
     * Function that returns a string with bit bits, 32 bits.
     * @return Returns 32 character string.
     */
    public String toString() {
        String bitsString = "";
        for (int i = 0; i < 32; i++) {
            if (i%8 == 0 && i != 0){
                //Separate each byte
                bitsString = " " + bitsString;
            }
            boolean index = this.bitSet.get(i);
            bitsString = (index?"1":"0") + bitsString;
        }
        return bitsString;
    }

    /**
     * Function that converts the BitsSet to a Integer.
     * @return Integer that represents the bits of the BitsSet.
     */
    public int toInt(){
        int valInt = 0;
        // Integer.SIZE = 32
        for(int i = 0; i < Integer.SIZE; i++){
            boolean bit = this.bitSet.get(i);
            if(bit){
                valInt += ((int)1 << i);
            }
        }
        return valInt;
    }

    /**
     * Method to deny the value of the bits from fromIndex to toIndex.
     * @param fromIndex Index where start to change the values.
     * @param toIndex Index after where values change.
     */
    public void flip(int fromIndex, int toIndex) {
        this.bitSet.flip(fromIndex, toIndex);
    }

    /**
     * Method that applies AND operation to the bits using other bits.
     * @param other Second operand for the AND.
     */
    public void and(BitsSet other) {
        this.bitSet.and(other.bitSet);
    }

    /**
     * Method that applies OR operation to the bits using other bits.
     * @param other Second operand for the OR.
     */
    public void or(BitsSet other) {
        this.bitSet.or(other.bitSet);
    }

    /**
     * Method that applies an XOR to the bits using other bits.
     * @param other Second operand for the XOR.
     */
    public void xor(BitsSet other) {
        this.bitSet.xor(other.bitSet);
    }

    /**
     * Method that applies a sum to the bits using other bits.
     * @param other Second operating for the sum.
     */
    public void add(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 + bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a subtraction to the bits using the other bits.
     * @param other Second operand for subtraction.
     */
    public void sub(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 - bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a multiplication to the bits using other bits.
     * @param other Second operand for multiplication.
     */
    public void mul(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 * bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a division to the bits using the other bits.
     * @param other Second operating for the division.
     */
    public void div(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 / bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies the module operation to the bits using the other bits.
     * @param other Second operand for the module.
     */
    public void mod(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 % bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies an arithmetic shift to the left (SAL) to the bits using the other bits.
     * @param other Second operand for the SAL, indicates the amount of shifts.
     */
    public void sal(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val << shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies an arithmetical shift to the right (SAR) to the bits using other bits.
     * @param other Second operating for the SAR, indicates the number of shifts.
     */
    public void sar(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a logical shift to the left (SLL) to the bits using other bits.
     * @param other Second operand for the SLL, indicates the number of slides.
     */
    public void sll(BitsSet other){
        this.sal(other);
    }

    /**
     * Method that applies a logical shift to the right (SLR) to the bits using other bits.
     * @param other Second operand for the SLR, indicates the number of slides.
     */
    public void slr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >>> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a circular shift to the left (SCL) to the bits using the other bits.
     * @param other Second operand for the SCL, indicates the number of slides.
     */
    public void scl(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val << shift) | (val >>> (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Method that applies a logical shift to the right (SCR) to the bits using other bits.
     * @param other Second operand for the SCR, indicates the amount of shifts.
     */
    public void scr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val >>> shift) | (val << (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Function that returns true if both BitsSets are equal.
     * @param other Second BitsSet to make the comparison.
     * @return True if this BitsSet is the same as the other BitsSet.
     */
    public boolean equals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            //Case where the number of bits is different
            return false;
        }
        //Case where the number of bits is the same
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                //Case where the bit i is different in the BitsSets
                return false;
            }
        }
        //Case where they are equal
        return true;
    }

    /**
     * Function that returns true if both BitsSets are not equal.
     * @param other Second BitsSet to make the comparison.
     * @return True if this BitsSet is not equal to the other BitsSet.
     */
    public boolean notEquals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            //Case where the number of bits is different
            return true;
        }
        //Case where the number of bits is the same
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                //Case where the bit i is different in the BitsSets
                return true;
            }
        }
        //Case where they are equal
        return false;
    }

    /**
     * Function that returns true this BitsSet is greater than the other BitsSet, comparison signed.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is greater than the other BitsSet.
     */
    public boolean greaterSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal > otherVal;
    }

    /**
     * Function that returns true this BitsSet is greater than the other BitsSet, comparison unsigned.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is greater than the other BitsSet.
     */
    public boolean greaterUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            //True if first longer than the second
            //False if first shorter than the second
            return thisSize > otherSize;
        }
        //Number of equal bits
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                //True if first in i is 1 and second is 0
                //False if first in i is 0 and second is 1
                return thisVal;
            }
        }
        //False because they are the same
        return false;
    }

    /**
     * Function that returns true this BitsSet is greater than or equal to the other BitsSet, comparison with signed.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is greater than or equal to the other BitsSet.
     */
    public boolean greaterOrEqualSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal >= otherVal;
    }

    /**
     * Function that returns true this BitsSet is greater than or equal to the other BitsSet, comparison unsigned.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is greater than or equal to the other BitsSet.
     */
    public boolean greaterOrEqualUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            //False if first shorter than the second
            //True if Ffrst longer than the second
            return thisSize > otherSize;
        }
        //Number of equal bits
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                //True if first in i is 1 and second is 0
                //False if first in i is 0 and second is 1
                return thisVal;
            }
        }
        //False because they are the same
        return true;
    }

    /**
     * Function that returns true this BitsSet is less than the other BitsSet, comparison signed.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is less than the other BitsSet.
     */
    public boolean lessSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal < otherVal;
    }

    /**
     * Function that returns true this BitsSet is less than the other BitsSet, comparison unsigned.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is less than the other BitsSet.
     */
    public boolean lessUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            //True if first shorter than the second
            //False if first longer than the second
            return thisSize < otherSize;
        }
        //Number of equal bits
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                //True if first in i is 0 and second is 1
                //False if first in i is 1 and second is 0
                return !thisVal;
            }
        }
        //False because they are the same
        return false;
    }

    /**
     * Function that returns true this BitsSet is less or equal to the other BitsSet, comparison signed.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is less than or equal to the other BitsSet.
     */
    public boolean lessOrEqualSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal <= otherVal;
    }

    /**
     * Function that returns true this BitsSet is less than or equal to the other BitsSet, comparison signed.
     * @param other Second BitsSet for comparison.
     * @return True if this BitsSet is less than or equal to the other BitsSet.
     */
    public boolean lessOrEqualUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            //True if first shorter than the second
            //False if first longer than the second
            return thisSize < otherSize;
        }
        //Number of equal bits
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                //True if first in i is 0 and second is 1
                //False if first in i is 1 and second is 0
                return !thisVal;
            }
        }
        //False because they are the same
        return true;
    }

}


