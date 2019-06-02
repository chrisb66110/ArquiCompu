package com.simulacion;

import java.util.BitSet;

public class BitsSet{
    private BitSet bitSet;

    /**
     * Constructor a partir de BitSet
     */
    public BitsSet(BitSet bitSet){
        this.bitSet = bitSet;
    }

    public BitsSet(){
        this.bitSet = new BitSet();
    }

    /**
     * Construir con nbits bit
     */
    public BitsSet(int nbits) {
        this.bitSet = new BitSet(nbits);
    }

    public static BitsSet valueOf(int number) {
        //Se hace de 32 para manejar los shift
        BitsSet nuevo = new BitsSet(32);
        //Se llenan los 32 bits
        for (int i = 0; i <32; ++i) {
            nuevo.set(i,(number & (1 << i)) != 0);
        }
        return nuevo;
    }

    public void flip(int bitIndex) {
        this.bitSet.flip(bitIndex);
    }

    public void flip(int fromIndex, int toIndex) {
        this.bitSet.flip(fromIndex, toIndex);
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

    public boolean get(int bitIndex) {
        return this.bitSet.get(bitIndex);
    }

    public BitsSet get(int fromIndex, int toIndex) {
        return new BitsSet(this.bitSet.get(fromIndex, toIndex));
    }

    public int length() {
        return this.bitSet.length();
    }

    public boolean isEmpty(){
        return this.bitSet.isEmpty();
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

    public int size(){
        return this.bitSet.size();
    }

    public boolean equals(Object obj) {
        return this.bitSet.equals(obj);
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
        for(int i = 0; i < 32; i++){
            boolean bit = this.bitSet.get(i);
            if(bit){
                valint += ((int)1 << i) /*(long)Math.pow(2,i)*/;
                //System.out.println("Agrego 2^"+i+": " + ((long)1 << i));
            }
        }
        //System.out.println(Long.toBinaryString(valint));
        return valint;
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
        //System.out.println(this.bitSet.toString());
        //System.out.println(Long.toBinaryString(val));
        int resp = val >> shift;

        //System.out.println(Long.toBinaryString(resp));

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sll(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();

        int resp = val << shift;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
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

    /*public static String byteArrayToString(byte [] array){
        String x = "{";
        for (byte element: array) {
            x += element + ", ";
        }
        return x.substring(0,x.length()-2) + "}";
    }

    public static long byteArrayToInt(byte [] array){
        ByteBuffer wrapped = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);
        return wrapped.getLong();
    }

    public static byte [] longToByteArray(long num){
        ByteBuffer dbuf = ByteBuffer.allocate(8);
        dbuf.putLong(num);
        return dbuf.array();
    }*/



    public static void main(String[] args) {
        BitsSet bitSetX;
        BitsSet bitSetY;

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(215);
        System.out.println("Val1: " + bitSetX.toInt());
        System.out.println("Val2: " + bitSetY.toInt());
        bitSetX.add(bitSetY);
        System.out.println("Suma: " + bitSetX.toInt());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(215);
        System.out.println("Val1: " + bitSetX.toInt());
        System.out.println("Val2: " + bitSetY.toInt());
        bitSetX.sub(bitSetY);
        System.out.println("Rest: " + bitSetX.toInt());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(215);
        System.out.println("Val1: " + bitSetX.toInt());
        System.out.println("Val2: " + bitSetY.toInt());
        bitSetX.mul(bitSetY);
        System.out.println("Mult: " + bitSetX.toInt());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(215);
        System.out.println("Val1: " + bitSetX.toInt());
        System.out.println("Val2: " + bitSetY.toInt());
        bitSetX.div(bitSetY);
        System.out.println("Divi: " + bitSetX.toInt());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(215);
        System.out.println("Val1: " + bitSetX.toInt());
        System.out.println("Val2: " + bitSetY.toInt());
        bitSetX.mod(bitSetY);
        System.out.println("Mod : " + bitSetX.toInt());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.sal(bitSetY);
        System.out.println("Sal : " + bitSetX.toString());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.sar(bitSetY);
        System.out.println("Sar : " + bitSetX.toString());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.sll(bitSetY);
        System.out.println("Sll : " + bitSetX.toString());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.slr(bitSetY);
        System.out.println("Slr : " + bitSetX.toString());
        System.out.println();

        bitSetX = BitsSet.valueOf(-143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.scl(bitSetY);
        System.out.println("Scl : " + bitSetX.toString());
        System.out.println();

        bitSetX = BitsSet.valueOf(143257);
        bitSetY = BitsSet.valueOf(6);
        System.out.println("Val1: " + bitSetX.toString());
        System.out.println("Val2: " + bitSetY.toString());
        bitSetX.scr(bitSetY);
        System.out.println("Scr : " + bitSetX.toString());
        System.out.println();
        
    }
}
