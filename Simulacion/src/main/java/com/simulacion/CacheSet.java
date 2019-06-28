//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.Random;
//-----------------------------------------------------------------------------
/**
 * Class that emulates a set from an associative caché.
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
            this.blocks[index].valid = false;
        }
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
    // Methods
    /**
     * Find the data stored on the given address
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the address to look for
     * @return the data in the address if it's found, null otherwise
     */
    public BitsSet find(BitsSet address) {
        //---------------------------------------------------------------------
        // Auxiliary Variables
        BitsSet result = null;
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
            // update the index
            index++;
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        return result;
        //---------------------------------------------------------------------
    }
    /**
     * Method used for write the given data to the given address. 
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address
     * @param amount
     * @param data
     */
    public void writeBits(BitsSet address, OperandSize amount, BitsSet data) {
        //---------------------------------------------------------------------
        // Auxiliary variables.
        boolean updated = false;
        int index = 0;
        int invalidBlockIndex = -1;
        //---------------------------------------------------------------------
        while ((!updated) && (index < this.blocks.length)) {
            //-----------------------------------------------------------------
            // Getting the current caché block
            CacheBlock currentBlock = this.blocks[index];
            //-----------------------------------------------------------------
            // if the current block is a caché of the address
            if (currentBlock.tag == address) {
                currentBlock.data = data;
                updated = true;
            } else if (!currentBlock.valid) {
                //-------------------------------------------------------------
                // Here we save the index of the last invalid block in the
                // array. With this, we can know if there is a free space in
                // it for the given address to be cached.
                //  
                invalidBlockIndex = index;
                //-------------------------------------------------------------
            }
            //-----------------------------------------------------------------
            // updating the index
            index++;
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        // if the block was not update, we add it
        if (!updated) {
            //-----------------------------------------------------------------
            // checking if there is free spaces in the set
            int savingBlockIndex = -1;
            if (invalidBlockIndex != 1) {
                //-------------------------------------------------------------
                // since there is free space, the saving space is the last 
                // free space
                savingBlockIndex = invalidBlockIndex;
                //-------------------------------------------------------------
            } else {
                //-------------------------------------------------------------
                // otherwise, find a index victim
                // TODO: we are using a random algorithm by now
                Random rand = new Random();
                savingBlockIndex = rand.nextInt(this.blocks.length);
                //-------------------------------------------------------------
            }
            //-----------------------------------------------------------------
            // saving the block in the assigned index
            this.blocks[savingBlockIndex].data = BitsSet.valueOf(data.toInt());
            this.blocks[savingBlockIndex].tag = BitsSet.valueOf(address.toInt());
            this.blocks[savingBlockIndex].valid = true;
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
	}
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
