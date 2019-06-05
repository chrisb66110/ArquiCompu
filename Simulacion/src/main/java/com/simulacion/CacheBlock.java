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
    // Constructor
    public CacheBlock() {
        this.data = new BitSet();
        this.tag = new BitSet();
        this.valid = false;
    }
    //-------------------------------------------------------------------------
}
