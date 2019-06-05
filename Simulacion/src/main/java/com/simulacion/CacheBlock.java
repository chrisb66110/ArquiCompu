//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.BitSet;
//-----------------------------------------------------------------------------
/**
 * Class that emulates the behavior of a block inside a caché set
 */
public class CacheBlock {
    //-------------------------------------------------------------------------
    // Global Variables
    public BitSet data;
    public BitSet tag;
    public boolean valid;
    //-------------------------------------------------------------------------
    // Const.
    private final int TAG_SIZE = 32;
    //-------------------------------------------------------------------------
    // Constructor
    /**
     * Default constructor of the class.
     * Sets 
     */
    public CacheBlock(int blockSize) {
        this.data = new BitSet(blockSize);
        this.tag = new BitSet(TAG_SIZE);
        this.valid = false;
    }
    //-------------------------------------------------------------------------
}
