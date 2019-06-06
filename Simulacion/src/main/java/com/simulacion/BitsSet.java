package com.simulacion;

import java.util.BitSet;

public class BitsSet{
    private BitSet bitSet;

    public BitsSet(){
        this.bitSet = new BitSet();
    }

    public BitsSet(BitSet bitSet){
        this.bitSet = bitSet;
    }

    public BitsSet(int nbits) {
        this.bitSet = new BitSet(nbits);
    }

    public static BitsSet valueOf(int number) {
        //Integer.SIZE = 32
        BitsSet nuevo = new BitsSet(Integer.SIZE);
        for (int i = 0; i <Integer.SIZE; ++i) {
            nuevo.set(i,(number & (1 << i)) != 0);
        }
        return nuevo;
    }

    public boolean get(int bitIndex) {
        return this.bitSet.get(bitIndex);
    }

    public BitsSet get(int fromIndex, int toIndex) {
        return new BitsSet(this.bitSet.get(fromIndex, toIndex));
    }

    public void set(int bitIndex) {
        this.bitSet.set(bitIndex);
    }

    public void set(int bitIndex, boolean value) {
        this.bitSet.set(bitIndex, value);
    }

    public void set(int fromIndex, int toIndex) {
        this.bitSet.set(fromIndex, toIndex);
    }

    public void set(int fromIndex, int toIndex, boolean value) {
        this.bitSet.set(fromIndex, toIndex, value);
    }

    public void clear(int bitIndex) {
        this.bitSet.clear(bitIndex);
    }

    public void clear(int fromIndex, int toIndex) {
        this.bitSet.clear(fromIndex, toIndex);
    }

    public void clear() {
        this.bitSet.clear();
    }

    public int length() {
        return this.bitSet.length();
    }

    public boolean isEmpty(){
        return this.bitSet.isEmpty();
    }

    public int size(){
        return this.bitSet.size();
    }

    public Object clone() {
        return this.bitSet.clone();
    }

    public String toString() {
        String x = "";
        for (int i = 0; i < 32; i++) {
            if (i%8 == 0 && i != 0){
                x = " " + x;
            }
            boolean index = this.bitSet.get(i);
            x = (index?"1":"0") + x;
        }
        return x;
    }

    public int toInt(){
        int valint = 0;
        // Integer.SIZE = 32
        for(int i = 0; i < Integer.SIZE; i++){
            boolean bit = this.bitSet.get(i);
            if(bit){
                valint += ((int)1 << i);
            }
        }
        return valint;
    }

    public void flip(int bitIndex) {
        this.bitSet.flip(bitIndex);
    }

    public void flip(int fromIndex, int toIndex) {
        this.bitSet.flip(fromIndex, toIndex);
    }

    public void and(BitsSet set) {
        this.bitSet.and(set.bitSet);
    }

    public void or(BitsSet set) {
        this.bitSet.or(set.bitSet);
    }

    public void xor(BitsSet set) {
        this.bitSet.xor(set.bitSet);
    }

    public void andNot(BitsSet set) {
        this.bitSet.andNot(set.bitSet);
    }

    public void add(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 + bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sub(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 - bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void mul(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 * bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void div(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 / bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void mod(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 % bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sal(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val << shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sar(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sll(BitsSet other){
        this.sal(other);
    }

    public void slr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >>> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void scl(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val << shift) | (val >>> (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void scr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val >>> shift) | (val << (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public boolean equals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            return false;
        }
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean notEquals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            return true;
        }
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean greater(BitsSet other){
        //TODO: falta implementar
        //TODO: hablar con los compañeros a ver si este no importa el signo o se nos olvido
        return false;
    }

    public boolean greaterOrEqualSigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

    public boolean greaterOrEqualUnsigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

    public boolean lessSigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

    public boolean lessUnsigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

    public boolean lessOrEqualSigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

    public boolean lessOrEqualUnsigned(BitsSet other){
        //TODO: falta implementar
        return false;
    }

}
