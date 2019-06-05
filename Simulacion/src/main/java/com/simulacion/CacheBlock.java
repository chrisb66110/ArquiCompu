//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
/**
 * Class that emulates the behavior of a block inside a caché set
 */
public class CacheBlock {
    //-------------------------------------------------------------------------
    // Global Variables
    public BitsSet data;
    public BitsSet tag;
    public boolean valid;
    //-------------------------------------------------------------------------
    // Const.
    private final int TAG_SIZE = 32;
    //-------------------------------------------------------------------------
    // Constructor
    /**
     * Default constructor of the class.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param blockSize the block size
     */
    public CacheBlock(int blockSize) {
        this.data = new BitsSet(blockSize);
        this.tag = new BitsSet(TAG_SIZE);
        this.valid = false;
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------