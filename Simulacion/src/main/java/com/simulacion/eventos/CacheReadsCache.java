//-----------------------------------------------------------------------------
// Packages
package com.simulacion.eventos;
//-----------------------------------------------------------------------------
/**
 * This class is used for the caché to read from the next level
 * 
 * @author Joseph Rementería (b55824)
 */
public class CacheReadsCache extends Event {
    //-------------------------------------------------------------------------
    // Constructor
    /**
     * Constructor of hte class
     * 
     * @author Joseph Rementería (b552824)
     * 
     * @param cycles the time recured to finish the event
     * @param info the result of the reading. info[0] := BitsSet: data returned
     */
    public CacheReadsCache(int cycles, Object[] info) {
        super(cycles, "Cache retorna datos", info);
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------