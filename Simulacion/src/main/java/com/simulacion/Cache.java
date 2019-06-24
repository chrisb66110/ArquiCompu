//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import com.simulacion.eventos.CacheDataReturn;
import com.simulacion.eventos.CacheWroteData;
import com.simulacion.eventos.BusSendsSignal;
import com.simulacion.Consts;
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
    private int level;
    private int hitTime;
    private Cache nextCache;
    private Bus memoryBus;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus rxSubscriber = RxBus.getInstance();
    //-------------------------------------------------------------------------
    // Events
    private Subscription cacheReadsCache;
    private Subscription cacheReadsMemory;
    private Subscription cacheWritesCache;
    private Subscription cacheWriteMemory;
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
        int sets = 
            (int) Math.ceil(size/(Consts.BLOCK_SIZE/8 * this.asociativity));
        this.sets = new CacheSet[sets];
        for (int index = 0; index < sets; index++) {
            this.sets[index] = new CacheSet(
                this.asociativity,
                Consts.BLOCK_SIZE
            );
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
                        if (
                            this.level == 
                            (int) event.info[Consts.INFO_LEVEL_INDEX]
                        ) {                
                            //-------------------------------------------------
                            // saving it in this level (because principle of
                            // locality)
                            this.writeLocal(
                                address,
                                amount, 
                                (BitsSet) event.info[Consts.INFO_DATA_INDEX]
                            );
                            //-------------------------------------------------
                            // Creating the event to send the data
                            this.createDataReturnedEvent(
                                (BitsSet)event.info[Consts.INFO_DATA_INDEX]
                            );
                            //-------------------------------------------------
                        }
                        //-----------------------------------------------------
                    }
                );
                //-------------------------------------------------------------
                // getting the data from the next caché level
                this.nextCache.getBits(address, amount);
                //-------------------------------------------------------------
            } else {
                //-------------------------------------------------------------
                // getting the data from memory
                this.cacheReadsMemory = this.rxSubscriber.register(
                    BusSendsSignal.class, 
                    event -> {
                        //-----------------------------------------------------
                        // Checking if the reading has already finished.
                        if (
                            Consts.MEM_READING_DONE_CODE == 
                            this.memoryBus.getControlLines().toInt()
                        ) {
                            //-------------------------------------------------
                            // creating the event so the level above can has 
                            // read address. 
                            this.createDataReturnedEvent(
                                memoryBus.getDataLines()
                            );
                            //-------------------------------------------------
                        }
                        //-----------------------------------------------------
                    }
                );
                //-------------------------------------------------------------
                // Trying to set the control lines
                try {
                    //---------------------------------------------------------
                    // Setting the control lines with the correct code
                    this.memoryBus.setControl(
                        new BitsSet(
                            Consts.CONTROL_LINES_SIZE,
                            Consts.MEM_READ_QUERY_CODE
                        )
                    );
                    //---------------------------------------------------------
                    // Setting the address lines
                    this.memoryBus.setAddress(address);
                    this.memoryBus.sendSignal();
                    //---------------------------------------------------------
                } catch (Exception e) {
                    // nothing but find the real control or address lines size
                    System.out.println(e);
                }
                //-------------------------------------------------------------
            }
        } else {
            //-----------------------------------------------------------------
            // reduce the size of the reading. 
            result = this .trimToOperandSize(result,amount);
            //-----------------------------------------------------------------
            // Setting the result in the info array
            this.createDataReturnedEvent(result);
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        // return result;
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Creates the event to unlock the code from above
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param reading the BitsSet read from this level
     */
    private void createDataReturnedEvent(BitsSet reading) {
        //---------------------------------------------------------------------
        // Setting the result in the info array
        Object[] info = new Object[2];
        info[Consts.INFO_DATA_INDEX] = reading;
        info[Consts.INFO_LEVEL_INDEX] = this.level - 1;
        //---------------------------------------------------------------------
        // creating the event to the data to be read
        CacheDataReturn event = new CacheDataReturn(this.hitTime, info);
        this.eventHandler.addEvent(event);
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
    public void writeBits(BitsSet address, OperandSize amount, BitsSet data) {
        //---------------------------------------------------------------------
        // Update this level.
        this.writeLocal(address, amount, data);
        //---------------------------------------------------------------------
        // check if this is the last level caché
        if (this.nextCache != null) {
            //-----------------------------------------------------------------
            // Creating the event to write in the cache levels from bellow
            this.cacheWritesCache = this.rxSubscriber.register(
                CacheWroteData.class,
                event -> {
                    //---------------------------------------------------------
                    // Cheching whether the event is ours or not 
                    if (
                        this.level == 
                        (int) event.info[Consts.INFO_LEVEL_INDEX-1]
                    ) {
                        //-----------------------------------------------------
                        // Creates the event to unlock the previous level
                        this.createWriteEvent();
                        //-----------------------------------------------------
                    }
                    //---------------------------------------------------------
                }
            );
        } else {
            //-----------------------------------------------------------------
            // writting the data to memory
            this.cacheWriteMemory = this.rxSubscriber.register(
                BusSendsSignal.class, 
                event -> {
                    //---------------------------------------------------------
                    // Checking if the writting has already finished.
                    if (Consts.MEM_WRITING_DONE_CODE == this.memoryBus.getControlLines().toInt()) {
                        //-----------------------------------------------------
                        // creating the event so the level above knows the 
                        // write has finished
                        this.createWriteEvent();
                        //-----------------------------------------------------
                    }
                    //---------------------------------------------------------
                }
            );
            //-----------------------------------------------------------------
            // Trying to set the control lines
            try {
                //-------------------------------------------------------------
                // Setting the control lines with the correct code
                int code = -1;
                switch (amount) {
                    case Byte:
                        //-----------------------------------------------------
                        code = Consts.BYTE_SIZE;
                        break;
                        //-----------------------------------------------------
                    case HalfWord:
                        //-----------------------------------------------------
                        code = Consts.HALFWORD_SIZE;
                        break;
                        //-----------------------------------------------------
                    case Word:
                        //-----------------------------------------------------
                        code = Consts.WORD_SIZE;
                        break;
                        //-----------------------------------------------------
                }
                this.memoryBus.setControl(
                    new BitsSet(
                        Consts.CONTROL_LINES_SIZE,
                        code
                    )
                );
                //-------------------------------------------------------------
                // Setting the address lines
                this.memoryBus.setAddress(address);
                //-------------------------------------------------------------
                // Setting the data lines
                this.memoryBus.setData(data);
                this.memoryBus.sendSignal();
                //-------------------------------------------------------------
            } catch (Exception e) {
                // nothing but find the real control or address lines size
            }
            //-----------------------------------------------------------------
        }
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Creates the event to unlock the code from above
     * 
     * @author Joseph Rementería (b55824)
     */
    private void createWriteEvent() {
        //---------------------------------------------------------------------
        // Setting the result in the info array
        Object[] info = new Object[1];
        info[Consts.INFO_LEVEL_INDEX-1] = this.level - 1;
        //---------------------------------------------------------------------
        // Creating the event to the data to be read
        CacheWroteData event = new CacheWroteData(this.hitTime, info);
        this.eventHandler.addEvent(event);
        //---------------------------------------------------------------------
    }
    /**
     * Method that writes the given data in given address just for  this level.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the address to write the data
     * @param amount the size of the data to be written
     * @param data the data to be written
     * @return 
     */
    private void writeLocal(BitsSet address, OperandSize amount, BitsSet data){
        //---------------------------------------------------------------------
        // Find the set number assigned to the address
        int setNumber = this.calculateSetIndex(address);
        //---------------------------------------------------------------------
        // write the changes in the current caché level. 
        this.sets[setNumber].writeBits(address, amount, data);
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
        // Casting bitset to an integer
        int addressInt = address.toInt();
        //---------------------------------------------------------------------
        // computing the block number and the set index
        return (
            (int) Math.floor(addressInt/Consts.BLOCK_SIZE) % this.sets.length
        );
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
    /**
     * 
     * Trims the given value to the given size
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param value the value to trim
     * @param size the desired size
     * @return the value in the least significant bits of the desired size
     */
    private BitsSet trimToOperandSize(BitsSet value, OperandSize size) {
        //---------------------------------------------------------------------
        // reduce the size of the reading. 
        int bitsSetCutIndex = -1;
        BitsSet result = null;
        //---------------------------------------------------------------------
        switch (size) {
            case Word:
                bitsSetCutIndex = Consts.WORD_SIZE;
                break;
            case HalfWord:
                bitsSetCutIndex = Consts.HALFWORD_SIZE;
                break;
            case Byte:
                bitsSetCutIndex = Consts.BYTE_SIZE;
                break;
        }
        result = value.get(
            value.length()-bitsSetCutIndex, value.length()
        );
        //---------------------------------------------------------------------
        return result;
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------