//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import com.simulacion.eventos.CacheDataReturn;

import rx.Subscription;
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
    private int BLOCK_SIZE = 512;
    private int level;
    private int hitTime;
    private Cache nextCache;
    private Bus memoryBus;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus rxSubscriber = RxBus.getInstance();
    //-------------------------------------------------------------------------
    // Constants
    private final int INFO_DATA_INDEX = 0;
    private final int INFO_LEVEL_INDEX = 0;
    //-------------------------------------------------------------------------
    // Events
    private Subscription cacheReadsCache;
    //-------------------------------------------------------------------------
    // Constructors
    /**
     * Class constructor that receives the caché level and some aspects of
     * its capacity
     *
     * @author Joseph Rementería (b55824)
     * 
     * @param size
     * @param associativity
     * @param level
     * @param hitTime
     * @param nextCache
     * @param memoryBus
     */
    public Cache(
        long size,
        int associativity,
        int level,
        int hitTime,
        Cache nextCache, 
        Bus memoryBus
    ) {
        //---------------------------------------------------------------------
        // Setting the asocitivity of the level
        this.asociativity = associativity;
        //---------------------------------------------------------------------
        // Setting the size of the caché
        this.size = size;
        //---------------------------------------------------------------------
        // Setting the creation of the caché sets
        // this.size = this.BLOCK_SIZE * sets * this.asociativity;
        int sets = (int) Math.ceil(size/(BLOCK_SIZE * this.asociativity));
        this.sets = new CacheSet[sets];
        for (int index = 0; index < sets; index++) {
            this.sets[index] = new CacheSet(this.asociativity, BLOCK_SIZE);
        }
        //---------------------------------------------------------------------
        // Setting the level
        this.level = level;
        //---------------------------------------------------------------------
        // Setting the next caché level
        this.nextCache = nextCache;
        //---------------------------------------------------------------------
        // Setting memory bus
        this.memoryBus = memoryBus;
        //---------------------------------------------------------------------
        // Setting hit time
        this.hitTime = hitTime;
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
    public void getBits(BitsSet address, OperandSize amount){
        //---------------------------------------------------------------------
        // Auxiliary Vars.
        BitsSet result = null;
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
            // check if this is the last level caché
            if (this.nextCache != null) {
                //-------------------------------------------------------------
                this.cacheReadsCache = this.rxSubscriber.register(
                    CacheDataReturn.class, 
                    event -> {
                        //-----------------------------------------------------
                        // Check if the event has my information
                        if (this.level == (int) event.info[INFO_LEVEL_INDEX]) {                
                            //-------------------------------------------------
                            // saving it in this level 
                            // (because principle of locality)
                            this.writeLocal(
                                address,
                                amount, 
                                (BItsSet) info[this.INFO_DATA_INDEX]
                            );
                            //-------------------------------------------------
                            //-------------------------------------------------
                            this.cacheReadsCache.unsubscribe();
                            //-------------------------------------------------
                        }
                        //-----------------------------------------------------
                    }
                );
                //-------------------------------------------------------------
                // getting the data from the next caché level
                // TODO: create the event... and wait for another one
                this.nextCache.getBits(address, amount);
                //-------------------------------------------------------------
            } else {
                //-------------------------------------------------------------
                // getting the data from memory
                // TODO: fetch the data from memory and create the event...
                // TODO: and wait for another one
                //-------------------------------------------------------------
            }
        } else {
            //-----------------------------------------------------------------
            // reduce the size of the reading. 
            int bitsSetCutIndex = -1;
            switch (amount) {
                case Word:
                    bitsSetCutIndex = 32;
                    break;
                case HalfWord:
                bitsSetCutIndex = 16;
                    break;
                case Byte:
                bitsSetCutIndex = 8;
                    break;
            }
            result = result.get(
                result.length()-bitsSetCutIndex, result.length()
            );
            //-----------------------------------------------------------------
            // Setting the result in the info array
            Object[] info = new Object[2];
            info[this.INFO_DATA_INDEX] = result;
            info[this.INFO_LEVEL_INDEX] = this.level - 1;
            //-----------------------------------------------------------------
            // creating the event to the data to be read
            CacheDataReturn event = new CacheDataReturn(this.hitTime, info);
            this.eventHandler.addEvent(event);
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        // return result;
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
    public void writeBits(BitsSet address, OperandSize amount, BitsSet data){
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
            // TODO: copy from christopher xd
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
    private int calculateSetIndex (BitsSet address) {
        //---------------------------------------------------------------------
        // Auxiliary Vars.
        int addressInt = 0;
        //---------------------------------------------------------------------
        // Casting bitset to an integer
        // TODO: use the created one
        for (int index = address.length(); index >= 0 ; index--) {
            if (address.get(index)) {
                addressInt += Math.pow(2, index);
            }
        }
        //---------------------------------------------------------------------
        // computing the block number and the set index
        return (
            (int) Math.floor(addressInt/this.BLOCK_SIZE) % this.sets.length
        );
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------