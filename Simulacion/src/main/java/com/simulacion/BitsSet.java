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

    public static BitsSet valueOf(long number) {
        //Se hace de 64 para manejar los shift
        BitsSet nuevo = new BitsSet(64);
        //Se llenan los 32 bits
        for (int i = 0; i <32; ++i) {
            nuevo.set(i,(number & (1 << i)) != 0);
        }
        //Se llenan los otros con el ultimo bit
        nuevo.set(32,64,nuevo.get(31));
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

    public long toLong(){
        long valint = 0;
        for(int i = 0; i < 64; i++){
            boolean bit = this.bitSet.get(i);
            if(bit){
                valint += ((long)1 << i) /*(long)Math.pow(2,i)*/;
                //System.out.println("Agrego 2^"+i+": " + ((long)1 << i));
            }
        }
        //System.out.println(Long.toBinaryString(valint));
        return valint;
    }

    public void add(BitsSet other){
        long bitset1 = this.toLong();
        long bitset2 = other.toLong();

        long resp = bitset1 + bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sub(BitsSet other){
        long bitset1 = this.toLong();
        long bitset2 = other.toLong();

        long resp = bitset1 - bitset2;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void mul(BitsSet other){
        long bitset1 = this.toLong();
        long bitset2 = other.toLong();

        long resp = bitset1 * bitset2;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void div(BitsSet other){
        long bitset1 = this.toLong();
        long bitset2 = other.toLong();

        long resp = bitset1 / bitset2;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void mod(BitsSet other){
        long bitset1 = this.toLong();
        long bitset2 = other.toLong();

        long resp = bitset1 % bitset2;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sal(BitsSet other){
        long val = this.toLong();
        long shift = other.toLong();

        long resp = val << shift;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sar(BitsSet other){
        long val = this.toLong();
        long shift = other.toLong();
        //System.out.println(this.bitSet.toString());
        //System.out.println(Long.toBinaryString(val));
        long resp = val >> shift;

        //System.out.println(Long.toBinaryString(resp));

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void sll(BitsSet other){
        long val = this.toLong();
        long shift = other.toLong();

        long resp = val << shift;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void slr(BitsSet other){
        long val = this.toLong();
        long shift = other.toLong();

        long resp = val >>> 32 + shift;

        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    public void scl(BitsSet other){

    }

    public void scr(BitsSet other){

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

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.add(bitSetY);
        System.out.println("Suma: " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.sub(bitSetY);
        System.out.println("Rest: " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.mul(bitSetY);
        System.out.println("Mult: " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.div(bitSetY);
        System.out.println("Divi: " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.mod(bitSetY);
        System.out.println("Mod : " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.sal(bitSetY);
        System.out.println("Sal : " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.sar(bitSetY);
        System.out.println("Sar : " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.sll(bitSetY);
        System.out.println("Sll : " + bitSetX.toLong());
        System.out.println();

        bitSetX = BitsSet.valueOf(-1);
        bitSetY = BitsSet.valueOf(1);
        System.out.println("Val1: " + bitSetX.toLong());
        System.out.println("Val2: " + bitSetY.toLong());
        bitSetX.slr(bitSetY);
        System.out.println("Slr : " + bitSetX.toLong());
        System.out.println();

        /*
        int x = -1;
        System.out.println(x);

        System.out.println(Integer.toBinaryString(x));
        x = x >> 1;

        System.out.println(x);

        System.out.println(Integer.toBinaryString(x));*/
    }
}
