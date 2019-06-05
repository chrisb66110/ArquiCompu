//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.BitSet;
//-----------------------------------------------------------------------------
/**
 * Class that emulates a set from an associative caché.
 *
 * @author Joseph Rementería (b55824)
 * @version 1.0
 * @since 04-06-2019
 */
public class CacheSet {
    //-------------------------------------------------------------------------
    // Global Variables
    private CacheBlock[] blocks;
    //-------------------------------------------------------------------------
    // Constructors
    /**
     * Constructor of the class CacheSets.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param blocks the amount of blocks the set shall have
     * @param blockSize the size of each block
     */
    public CacheSet(int blocks, int blockSize){
        //---------------------------------------------------------------------
        // Creation of the array.
        this.blocks = new CacheBlock[blocks];
        //---------------------------------------------------------------------
        // Creation of each block
        for (int index = 0; index < blocks; index++) {
            this.blocks[index] = new CacheBlock(blockSize);
        }
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
    // Methods
    /**
     * Find the data stored on the given address
     * 
     * @author Joseph Rementería (b55824)
     * @since 04-06-2019
     * 
     * @param address the address to look for
     * @return the data in the address if it's found, null otherwise
     */
    public BitSet find(BitSet address) {
        //---------------------------------------------------------------------
        // Auxiliary Variables
        BitSet result = null;
        int index = 0;
        //---------------------------------------------------------------------
        // Searching for the address in the array
        while ((result == null) && (index < this.blocks.length)) {
            //-----------------------------------------------------------------
            // Get the current cache block
            CacheBlock currentCacheBlock = this.blocks[index];
            //-----------------------------------------------------------------
            // This if checks whether the address is in the current caché 
            // block or not and verifies if the data is valid
            if (
                (currentCacheBlock.tag == address) && 
                (currentCacheBlock.valid)
            ) {
                result = currentCacheBlock.data;
            }
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        return result;
    }
    //-------------------------------------------------------------------------
}
