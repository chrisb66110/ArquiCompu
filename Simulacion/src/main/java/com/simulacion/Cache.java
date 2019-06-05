//-----------------------------------------------------------------------------
// Paquete
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import java.util.BitSet;
//-----------------------------------------------------------------------------
/**
 * Clase que emula un chip de memoria caché en cualquiera de los niveles.
 *
 * @author Joseph Rementería (b55824)
 * @version 1.0
 * @since 04-06-2019
 */
public class Cache {
    //-------------------------------------------------------------------------
    // Variables globales
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
    // Constructores

    /**
     * Constructor de la clase que recibe el nivel de caché actual y otros
     * parámetros de configuración de capacidad.
     *
     * @param sets cantidad de sets en el nivel de caché
     * @param blockSize tamaño del bloque de memoria..
     * @param level el nivel de caché
     */
    public Cache(int sets, int blockSize, int level ){
        // TODO:  program this constructor using the methods
        //  of the other caché classes
    }
    //-------------------------------------------------------------------------
    // Métodos
    public BitSet[] getBits(BitSet address, OperandSize ammount){
        return null;
    }

    public void writeBits(BitSet address, OperandSize ammount, BitSet [] data){
        
    }
    //-------------------------------------------------------------------------
}
