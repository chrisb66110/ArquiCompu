//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.BitSet;
//-----------------------------------------------------------------------------
/**
 * Class that emulates the behavoir of a caché chip
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
     * @author Joseph Rementería (b55824)
     * 
     * @param sets amount of caché sets
     * @param blockSize block size
     * @param level caché level
     */
    public Cache(int sets, int blockSize, int level ){
        //---------------------------------------------------------------------
        // Setting the asocitivity of the level
        // TODO: find the correct name
        this.asociativity = 7;
        //---------------------------------------------------------------------
        // Setting the size of the caché
        this.size = blockSize * sets * this.asociativity;
        //---------------------------------------------------------------------
        // Setting the creation of the caché sets
        this.sets = new CacheSet[sets];
        for (int index = 0; index < sets; index++) {
            this.sets[index] = new CacheSet(this.asociativity, blockSize);
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
     * Reads a block of the given size from the given address
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the requested address
     * @param amount the size to read from memory/caché
     * @return  the data as a bitset if the address is 
     *          the current level, null otherwise
     */
    public BitSet getBits(BitSet address, OperandSize amount){
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
            // the data to be saved in this level for the next read
            BitSet dataFromBelow = null;
            //-----------------------------------------------------------------
            // check if this is the last level caché
            //-----------------------------------------------------------------
            if (this.nextCache != null) {
                //-------------------------------------------------------------
                // getting the data from the next caché level
                dataFromBelow = this.nextCache.getBits(address, amount);
                //-------------------------------------------------------------
            } else {
                //-------------------------------------------------------------
                // getting the data from memory
                // TODO:fetch the data from memory
                //-------------------------------------------------------------
            }
            //-----------------------------------------------------------------
            // saving it in this level (because principle of locality)
            this.writeBits(address, amount, dataFromBelow);
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        // TODO: cut this to the given amount of bits
        return result;
        //---------------------------------------------------------------------
    }
    /**
     * Method that writes the given data in given address. All the levels 
     * shall be updated and the data must be changed in memory too.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the address to write the data
     * @param amount the size of the data to be written
     * @param data the data to be written
     * @return 
     */
    public void writeBits(BitSet address, OperandSize amount, BitSet data){
        //---------------------------------------------------------------------
        // Find the set number assigned to the address
        int setNumber = this.calculateSetIndex(address);
        //---------------------------------------------------------------------
        // write the changes in the current caché level. 
        this.sets[setNumber].writeBits(address, amount, data);
        //---------------------------------------------------------------------
        // check if this is the last level caché
        if (this.nextCache != null) {
            //-----------------------------------------------------------------
            // write the changes in the next level.
            this.nextCache.writeBits(address, amount, data);
            //-----------------------------------------------------------------
        } else {
            //-----------------------------------------------------------------
            // write the changes in memory
            // TODO: write the changes in memory
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
    }
    /**
     * Calculate the set index form the given address
     * 
     * @author Joseph Rementería (b55824)
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
        return (
            (int) Math.floor(addressInt/this.blockSize) % this.sets.length
        );
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------