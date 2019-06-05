//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.BitSet;
//-----------------------------------------------------------------------------
/**
 * Class that emulates the behavoir of a caché chip
 *
 * @author Joseph Rementería (b55824)
 * @version 1.0
 * @since 04-06-2019
 */
public class Cache {
    //-------------------------------------------------------------------------
    // Global Variables
    private long size;
    private int asociativity;
    private CacheSet[] sets;
    private int blockSize;
    private int level;
    private Cache nextCache;
    private Bus memoryBus;
    private int hitTime;
    private EventsHandler eventHandler;
    //-------------------------------------------------------------------------
    // Constructors
    /**
     * Class constructor that receives the caché level and some aspects of
     * its capacity
     *
     * @param sets amount of caché sets
     * @param blockSize block size
     * @param level caché level
     */
    public Cache(int sets, int blockSize, int level ){
        // TODO:  program this constructor using the methods
        //  of the other caché classes
    }
    //-------------------------------------------------------------------------
    // Methods
    public BitSet[] getBits(BitSet address, OperandSize ammount){
        return null;
    }

    public void writeBits(BitSet address, OperandSize ammount, BitSet [] data){

    }
    //-------------------------------------------------------------------------
}
