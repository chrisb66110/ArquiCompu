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
    public CacheSet(int blocks){
        //---------------------------------------------------------------------
        // Creation of the array.
        this.blocks = new CacheBlock[blocks];
        //---------------------------------------------------------------------
        // Creation of each block
        for (int index = 0; index < blocks; index++) {
            this.blocks[index] = new CacheBlock();
        }
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
    // Methods
    public BitSet find(BitSet address){
        return null;
    }
    //-------------------------------------------------------------------------
}
