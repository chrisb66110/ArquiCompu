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
    /**
     * 
     */
    /**
     * Reads a block of the given size from the given address
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the requested address
     * @param ammount the size to read from memory/caché
     * @return  the data as a bitset if the address is 
     *          the current level, null otherwise
     */
    public BitSet getBits(BitSet address, OperandSize ammount){
        //---------------------------------------------------------------------
        // Auxiliary Vars.
        BitSet result = null;
        //---------------------------------------------------------------------
        // Calculate the set index to the given address
        int setIndex = this.calculateSetIndex(address);
        //---------------------------------------------------------------------
        // try to fetch the data
        result = this.sets[setIndex].find(address);
        //---------------------------------------------------------------------
        // if the data was not in this level then fetch it from the next level
        if (result == null){
            //-----------------------------------------------------------------
            // getting the data from the next caché level
            BitSet dataFromBelow = this.nextCache.getBits(address, ammount);
            //-----------------------------------------------------------------
            // saving it in this level
            this.writeBits(address, ammount, dataFromBelow);
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        return result;
        //---------------------------------------------------------------------
    }

    public void writeBits(BitSet address, OperandSize ammount, BitSet data){
        
    }
    //-------------------------------------------------------------------------
    /**
     * Calculate the set index form the given address
     * 
     * @author Joseph Rementería (b55824)
     * 
     * 
     * @param address the address to read/write
     * @return the set number assigned the given address
     */
    private int calculateSetIndex (BitSet address) {
        //---------------------------------------------------------------------
        // Auxiliary Vars.
        int addressInt = 0;
        //---------------------------------------------------------------------
        // Casting bitset to an integer
        for (int index = address.length(); index >= 0 ; index--) {
            if (address.get(index)) {
                addressInt += Math.pow(2, index);
            }
        }
        //---------------------------------------------------------------------
        // computing the block number and the set index
        return ((int) Math.floor(addressInt/this.blockSize) % this.sets.length);
    }
}
