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
    // Const.
    private int BLOCK_AMOUNT = 7;
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
        //---------------------------------------------------------------------
        // Setting the size of the caché
        this.size = blockSize * sets * this.BLOCK_AMOUNT;
        //---------------------------------------------------------------------
        // Setting the asocitivity of the level
        // TODO: find the correct name
        this.asociativity = 7;
        //---------------------------------------------------------------------
        // Setting the creation of the caché sets
        this.sets = new CacheSet[sets];
        for (int index = 0; index < sets; index++) {
            this.sets[index] = new CacheSet(BLOCK_AMOUNT, blockSize);
        }
        //---------------------------------------------------------------------
        // Setting the block size
        this.blockSize = blockSize;
        //---------------------------------------------------------------------
        // Setting the level
        this.level = level;
        //---------------------------------------------------------------------
        // Setting the next caché level
        // TODO: find this thing
        // this.nextCache = ;
        //---------------------------------------------------------------------
        // TODO: find this thing
        //this.memoryBus = ;
        //---------------------------------------------------------------------
        // TODO: find this thing
        // this.hitTime = ;
        //---------------------------------------------------------------------
        // TODO: find this thing
        // this.eventHandler = ;
        //---------------------------------------------------------------------
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
