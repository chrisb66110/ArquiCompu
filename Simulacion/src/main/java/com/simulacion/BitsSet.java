package com.simulacion;

import java.util.BitSet;

/**
 * Clase para el manejo de bits
 */
public class BitsSet{

    private BitSet bitSet; //Clase para manejar bits

    /**
     * Constructor sin parametros
     */
    public BitsSet(){
        this.bitSet = new BitSet();
    }

    /**
     * Constructor a partir de un BitSet
     * @param bitSet BitSet con el que desea construir el BitsSet
     */
    public BitsSet(BitSet bitSet){
        this.bitSet = bitSet;
    }

    /**
     * Constructor con n bits
     * @param nbits Cantidad de bits para el bitsset
     */
    public BitsSet(int nbits) {
        this.bitSet = new BitSet(nbits);
    }

    /**
     * Funcion estatica para construir un BitsSet a partir de un numero entero
     * Convierte el numero en bits para ir agregandolos a la estructura
     * @param number Entero que desea tener en el BitsSet
     * @return Retorna un objeto BitsSet que contiene los bits del numero del parametro
     */
    public static BitsSet valueOf(int number) {
        //Integer.SIZE = 32
        BitsSet nuevo = new BitsSet(Integer.SIZE);
        for (int i = 0; i <Integer.SIZE; ++i) {
            nuevo.set(i,(number & (1 << i)) != 0);
        }
        return nuevo;
    }

    /**
     * Funcion que retorna el valor de la posicion bitIndex en la estructura
     * @param bitIndex Posicion de bit que desea conocer
     * @return Booleano con el valor de la posicion bitIndex
     */
    public boolean get(int bitIndex) {
        return this.bitSet.get(bitIndex);
    }

    /**
     * Funcion que retorna un BitsSet con los bits indicados desde fromIndex hasta toIndex,
     * iniciando en la posicion 0 con el valor de la posicion fromIndex
     * @param fromIndex Posicion donde inicia, incluye el valor de esta posicion
     * @param toIndex Posicion siguiente a la que termina, es decir, excluye esta posicion
     * @return Retorna BitsSet con los bits indicados desde fromIndex hasta toIndex
     */
    public BitsSet get(int fromIndex, int toIndex) {
        return new BitsSet(this.bitSet.get(fromIndex, toIndex));
    }

    /**
     * Metodo para cambiar el valor del bit de la posicion bitIndex
     * @param bitIndex Posicion que desea cambiar el valor
     * @param value Valor nuevo
     */
    public void set(int bitIndex, boolean value) {
        this.bitSet.set(bitIndex, value);
    }

    /**
     * Funcion para saber la cantidad de bits utilizados
     * @return Cantidad de bits utilizados
     */
    public int length() {
        return this.bitSet.length();
    }

    /**
     * Funcion que retorna una copia del objeto
     * @return Retorna copia del objeto
     */
    public Object clone() {
        return this.bitSet.clone();
    }

    /**
     * Funcion que retorna una string con los bits del bitset, 32 bits
     * @return Retorna string de 32 caracteres
     */
    public String toString() {
        String x = "";
        for (int i = 0; i < 32; i++) {
            if (i%8 == 0 && i != 0){
                //Separa cada byte
                x = " " + x;
            }
            boolean index = this.bitSet.get(i);
            x = (index?"1":"0") + x;
        }
        return x;
    }

    /**
     * Funcion que convierte el BitsSet a entero
     * @return Entero que representa los bits del BitsSet
     */
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

    /**
     * Metodo para cambiar el negar el valor de los bits desde fromIndex hasta toIndex
     * @param fromIndex Indice donde inicia a cambiar los valores
     * @param toIndex Indice despues de donde termina de cambiar los valores
     */
    public void flip(int fromIndex, int toIndex) {
        this.bitSet.flip(fromIndex, toIndex);
    }

    /**
     * Metodo que le aplica operacion AND a los bits utilizando los bits de other
     * @param other Segundo operando para el AND
     */
    public void and(BitsSet other) {
        this.bitSet.and(other.bitSet);
    }

    /**
     * Metodo que le aplica operacion OR a los bits utilizando los bits de other
     * @param other Segundo operando para el OR
     */
    public void or(BitsSet other) {
        this.bitSet.or(other.bitSet);
    }

    /**
     * Metodo que le aplica un XOR a los bits utilizando los bits de other
     * @param other Segundo operando para el XOR
     */
    public void xor(BitsSet other) {
        this.bitSet.xor(other.bitSet);
    }

    /**
     * Metodo que le aplica una suma a los bits utilizando los bits de other
     * @param other Segundo operando para la suma
     */
    public void add(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 + bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica una resta a los bits utilizando los bits de other
     * @param other Segundo operando para la resta
     */
    public void sub(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 - bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica una multiplicacion a los bits utilizando los bits de other
     * @param other Segundo operando para la multiplicacion
     */
    public void mul(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 * bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica una divicion a los bits utilizando los bits de other
     * @param other Segundo operando para la divicion
     */
    public void div(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 / bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica la operacion modulo a los bits utilizando los bits de other
     * @param other Segundo operando para el modulo
     */
    public void mod(BitsSet other){
        int bitset1 = this.toInt();
        int bitset2 = other.toInt();
        int resp = bitset1 % bitset2;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica un corrimiento aritmético hacia la izquierda(SAL) a los bits utilizando los bits de other
     * @param other Segundo operando para el SAL, indica la cantidad de corrimientos.
     */
    public void sal(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val << shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica un corrimiento aritmético hacia la derecha(SAR) a los bits utilizando los bits de other
     * @param other Segundo operando para el SAR, indica la cantidad de corrimientos.
     */
    public void sar(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica un corrimiento lógico hacia la izquierda(SLL) a los bits utilizando los bits de other
     * @param other Segundo operando para el SLL, indica la cantidad de corrimientos.
     */
    public void sll(BitsSet other){
        this.sal(other);
    }

    /**
     * Metodo que le aplica un corrimiento lógico hacia la derecha(SLR) a los bits utilizando los bits de other
     * @param other Segundo operando para el SLR, indica la cantidad de corrimientos.
     */
    public void slr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = val >>> shift;
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica un corrimiento circular hacia la izquierda(SCL) a los bits utilizando los bits de other
     * @param other Segundo operando para el SCL, indica la cantidad de corrimientos.
     */
    public void scl(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val << shift) | (val >>> (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Metodo que le aplica un corrimiento lógico hacia la derecha(SCR) a los bits utilizando los bits de other
     * @param other Segundo operando para el SCR, indica la cantidad de corrimientos.
     */
    public void scr(BitsSet other){
        int val = this.toInt();
        int shift = other.toInt();
        int resp = (val >>> shift) | (val << (Integer.SIZE - shift));
        this.bitSet = BitsSet.valueOf(resp).bitSet;
    }

    /**
     * Funcion que retorna verdadero si ambos BitsSet son iguales
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es igual al otro BitsSet
     */
    public boolean equals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            //Caso donde la cantidad de bits es distinta
            return false;
        }
        //Caso donde la cantidad de bits es igual
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                //Caso donde el bit i es distinto en los BitsSets
                return false;
            }
        }
        //Caso donde son igaules
        return true;
    }

    /**
     * Funcion que retorna verdadero si ambos BitsSet no son iguales
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet no es igual al otro BitsSet
     */
    public boolean notEquals(BitsSet other) {
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize ) {
            //Caso donde la cantidad de bits es distinta
            return true;
        }
        //Caso donde la cantidad de bits es la misma
        for (int i = 0; i < thisSize; i++) {
            if (this.get(i) != other.get(i)) {
                //Caso donde el bit i es distinto en los BitsSets
                return true;
            }
        }
        //Caso donde son iguales
        return false;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es mayor que el otro BitsSet, comparacion sin signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es mayor que el otro BitsSet
     */
    public boolean greater(BitsSet other){
        //TODO: hablar con los compas si hago esto por comparacion unsigned(asi está)
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            if ( thisSize < otherSize ) {
                //Primero mas corto que el segundo
                return false;
            }else{
                //Primero mas largo que el segundo
                return true;
            }
        }
        //Caso de cantidad de bits iguales
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                if(thisVal){
                    //Primero en i es 1 y segundo es 0
                    return true;
                }else{
                    //Primero en i es 0 y segundo es 1
                    return false;
                }
            }
        }
        //Falso porque son iguales
        return false;
        //TODO: hablar con los compañeros a ver si este no importa el signo o se nos olvido
    }

    /**
     * Funcion que retorna verdadero este BitsSet es mayor o igual que el otro BitsSet, comparacion con signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es mayor o igual que el otro BitsSet
     */
    public boolean greaterOrEqualSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal >= otherVal;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es mayor o igual que el otro BitsSet, comparacion sin signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es mayor o igual que el otro BitsSet
     */
    public boolean greaterOrEqualUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            if ( thisSize < otherSize ) {
                //Primero mas corto que el segundo
                return false;
            }else{
                //Primero mas largo que el segundo
                return true;
            }
        }
        //Caso de cantidad de bits iguales
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                if(thisVal){
                    //Primero en i es 1 y segundo es 0
                    return true;
                }else{
                    //Primero en i es 0 y segundo es 1
                    return false;
                }
            }
        }
        //Falso porque son iguales
        return true;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es menor que el otro BitsSet, comparacion con signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es menor que el otro BitsSet
     */
    public boolean lessSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal < otherVal;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es menor que el otro BitsSet, comparacion sin signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es menor que el otro BitsSet
     */
    public boolean lessUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            if ( thisSize < otherSize ) {
                //Primero mas corto que el segundo
                return true;
            }else{
                //Primero mas largo que el segundo
                return false;
            }
        }
        //Caso de cantidad de bits iguales
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                if(!thisVal){
                    //Primero en i es 0 y segundo es 1
                    return true;
                }else{
                    //Primero en i es 0 y segundo es 1
                    return false;
                }
            }
        }
        //Falso porque son iguales
        return false;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es menor o igual que el otro BitsSet, comparacion con signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es menor o igual que el otro BitsSet
     */
    public boolean lessOrEqualSigned(BitsSet other){
        int thisVal = this.toInt();
        int otherVal = other.toInt();
        return thisVal <= otherVal;
    }

    /**
     * Funcion que retorna verdadero este BitsSet es menor o igual que el otro BitsSet, comparacion sin signo
     * @param other Segundo BitsSet para realizar la comparacion
     * @return True si este BitsSet es menor o igual que el otro BitsSet
     */
    public boolean lessOrEqualUnsigned(BitsSet other){
        int thisSize = this.length();
        int otherSize = other.length();
        if ( thisSize != otherSize) {
            if ( thisSize < otherSize ) {
                //Primero mas corto que el segundo
                return true;
            }else{
                //Primero mas largo que el segundo
                return false;
            }
        }
        //Caso de cantidad de bits iguales
        for (int i = 0; i < thisSize; i++) {
            boolean thisVal = this.get(i);
            boolean otherVal = other.get(i);
            if (thisVal != otherVal) {
                if(!thisVal){
                    //Primero en i es 0 y segundo es 1
                    return true;
                }else{
                    //Primero en i es 0 y segundo es 1
                    return false;
                }
            }
        }
        //Falso porque son iguales
        return true;
    }

}


