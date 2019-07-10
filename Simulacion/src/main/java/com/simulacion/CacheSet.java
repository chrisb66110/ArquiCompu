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
    private int blockSize;
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
        this.blockSize = blockSize;
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
     * Find the data stored on the given address. Returns the entire block
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the address to look for
     * @return the data in the address if it's found, null otherwise
     */
    public BitsSet find(BitsSet address) {
        //---------------------------------------------------------------------
        BitsSet memoryBlockAddress = BitsSet.valueOf(address.toInt()/(this.blockSize/8));
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
                (currentCacheBlock.tag.equals(memoryBlockAddress)) &&
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
     * @param data
     */
    public void writeBits(BitsSet address, BitsSet data) {
        BitsSet memoryBlockAddress = BitsSet.valueOf(address.toInt()/(this.blockSize/8));
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
            if (currentBlock.tag.equals(memoryBlockAddress)) {
                currentBlock.data = data.clone();
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
            if (invalidBlockIndex != -1) {
                //-------------------------------------------------------------
                // since there is free space, the saving space is the last 
                // free space
                savingBlockIndex = invalidBlockIndex;
                //-------------------------------------------------------------
            } else {
                //-------------------------------------------------------------
                // otherwise, find a index victim
                Random rand = new Random();
                savingBlockIndex = rand.nextInt(this.blocks.length);
                //-------------------------------------------------------------
            }
            //-----------------------------------------------------------------
            // saving the block in the assigned index
            this.blocks[savingBlockIndex].data = data.clone();
            this.blocks[savingBlockIndex].tag = memoryBlockAddress.clone();
            this.blocks[savingBlockIndex].valid = true;
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
	}

    /**
     * Updates the cache with the new data. If the block for this write is not present, it does nothing.
     * (Write Non-Allocate)
     *
     * @param address the memory address where the new data is being updated.
     * @param amount the ammount to be updated
     * @param data the data being updated
     */
    public void updateBlock(BitsSet address, OperandSize amount, BitsSet data) {
        BitsSet memoryBlockAddress = BitsSet.valueOf(address.toInt()/(this.blockSize/8));
        // Auxiliary Variables
        int index = 0;
        //---------------------------------------------------------------------
        // Searching for the address in the array
        while (index < this.blocks.length) {
            //-----------------------------------------------------------------
            if (this.blocks[index].tag.equals(memoryBlockAddress) && this.blocks[index].valid) {
                // Calculate offset between address and the start of block address
                int addressBlockOffset = address.toInt() - memoryBlockAddress.toInt() * (this.blockSize/8);
                // Extract the part of the data we need
                BitsSet exactData = data.get(0, amount.size);
                // Insert the data into the block part
                this.blocks[index].data.insertBits(this.blockSize - (addressBlockOffset * 8) - 1, exactData);
                break;
            }
            //-----------------------------------------------------------------
            // update the index
            index++;
            //-----------------------------------------------------------------
        }
    }

    /**
     * Sets the blocks of the set as invalid
     */
    public void setInvalid() {
        for (CacheBlock block : this.blocks) {
            block.valid = false;
        }
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
