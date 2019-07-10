//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// Imports
import com.simulacion.eventos.CacheDataReturn;
import com.simulacion.eventos.CacheHitOrMiss;
import com.simulacion.eventos.CacheWroteData;
import com.simulacion.eventos.BusSendsSignal;
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
    private int blockSize;
    private Cache nextCache;
    private Bus memoryBus;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus rxSubscriber = RxBus.getInstance();
    //-------------------------------------------------------------------------
    // Events
    private Subscription cacheDataReturn;
    private Subscription cacheReadsMemory;
    private Subscription cacheWroteData;
    private Subscription cacheWriteMemory;
    private Subscription cacheHitOrMiss;
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
        int blockSize,
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
        this.blockSize = blockSize;
        int sets = (int) Math.ceil(size/(this.blockSize/8 * this.asociativity));
        this.sets = new CacheSet[sets];
        for (int index = 0; index < sets; index++) {
            this.sets[index] = new CacheSet(
                this.asociativity,
                this.blockSize
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
    public void getBits(BitsSet address, OperandSize amount) {
        //---------------------------------------------------------------------
        // Calculate the set index to the given address
        int setIndex = this.calculateSetIndex(address);

        this.cacheHitOrMiss = this.rxSubscriber.register(CacheHitOrMiss.class, event -> {
            this.manageHitOrMiss(setIndex, address, amount);
            this.cacheHitOrMiss.unsubscribe();
        });

        this.eventHandler.addEvent(new CacheHitOrMiss(this.hitTime, null));
    }

    private void manageHitOrMiss(int setIndex, BitsSet address, OperandSize amount) {
        //---------------------------------------------------------------------
        // try to fetch the data
        BitsSet result = this.sets[setIndex].find(address);
        //---------------------------------------------------------------------
        // if the data was not in this level then fetch it from the next level
        if (result == null){
            //-----------------------------------------------------------------
            // check if this is the last level caché
            if (this.nextCache != null) {
                //-------------------------------------------------------------
                this.cacheDataReturn = this.rxSubscriber.register(
                    CacheDataReturn.class, 
                    event -> {
                        //-----------------------------------------------------
                        // Check if the event has my information
                        if (
                            this.level == 
                            (int) event.info[Consts.INFO_LEVEL_INDEX] &&
                                    ((BitsSet)event.info[Consts.INFO_ADDRESS]).equals(address)
                        ) {                
                            //-------------------------------------------------
                            // saving the block from lower level
                            this.writeLocal(
                                address,
                                (BitsSet) event.info[Consts.INFO_DATA_INDEX]
                            );
                            //-------------------------------------------------
                            // Creating the event to send the data
                            this.createDataReturnedEvent(
                                (BitsSet)event.info[Consts.INFO_DATA_INDEX],
                                    (BitsSet)event.info[Consts.INFO_ADDRESS]
                            );
                            //-------------------------------------------------
                            this.cacheDataReturn.unsubscribe();
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
                        // TODO: go various times to memory if the block size requires it
                        //-----------------------------------------------------
                        // Checking if the reading has already finished.
                        if (
                            Consts.MEM_READING_DONE_CODE == 
                            this.memoryBus.getControlLines().toInt()
                        ) {
                            // TODO: write local what memory gave me
                            //-------------------------------------------------
                            // creating the event so the level above can has 
                            // read address. 
                            this.createDataReturnedEvent(
                                memoryBus.getDataLines(),
                                    memoryBus.getAddressLines()
                            );
                            //-------------------------------------------------
                            this.cacheReadsMemory.unsubscribe();
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
            // TODO: always return the entire block except for level 1 cache who has to trim
            System.out.println("Had it in cache " + this.level);
            //-----------------------------------------------------------------
            // reduce the size of the reading. 
            result = this.trimToOperandSize(result,amount);
            //-----------------------------------------------------------------
            // Setting the result in the info array
            this.createDataReturnedEvent(result, BitsSet.valueOf(address.toInt()));
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
    private void createDataReturnedEvent(BitsSet reading, BitsSet address) {
        //---------------------------------------------------------------------
        // Setting the result in the info array
        Object[] info = new Object[3];
        info[Consts.INFO_DATA_INDEX] = reading;
        info[Consts.INFO_LEVEL_INDEX] = this.level - 1;
        info[Consts.INFO_ADDRESS] = address;
        //---------------------------------------------------------------------
        // creating the event to the data to be read
        CacheDataReturn event = new CacheDataReturn(1, info);
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
        if(this.sets[this.calculateSetIndex(address)].find(address) != null)
            this.updateLocal(address, amount, data);
        else
            // TODO: mark block as non valid. Non-allocate write
        //---------------------------------------------------------------------
        // check if this is the last level caché
        if (this.nextCache != null) {
            //-----------------------------------------------------------------
            // Creating the event to write in the cache levels from bellow
            this.cacheWroteData = this.rxSubscriber.register(
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
                        this.cacheWroteData.unsubscribe();
                    }
                    //---------------------------------------------------------
                }
            );
            //Write to the next level
            this.nextCache.writeBits(address, amount, data);
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
                        this.cacheWriteMemory.unsubscribe();
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
                        code = Consts.MEM_WRITE_BT_QUERY_CODE;
                        break;
                        //-----------------------------------------------------
                    case HalfWord:
                        //-----------------------------------------------------
                        code = Consts.MEM_WRITE_HW_QUERY_CODE;
                        break;
                        //-----------------------------------------------------
                    case Word:
                        //-----------------------------------------------------
                        code = Consts.MEM_WRITE_WD_QUERY_CODE;
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
                System.out.println(e);
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
     * Method that writes the given data in the respective block(s)
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address the address to write the data
     * @param data the data to be written provided by either a lower level cache or memory
     * @return 
     */
    private void writeLocal(BitsSet address, BitsSet data){
        //---------------------------------------------------------------------
        // Find the set number assigned to the address
        int setNumber = this.calculateSetIndex(address);
        //---------------------------------------------------------------------
        // write the changes in the current caché level. 
        this.sets[setNumber].writeBits(address, data);
        //---------------------------------------------------------------------
    }

    /**
     * Method that updates the respective part of the block. This method assumes the cache already
     * checked that the block for this address is present
     *
     * @param address The address of memory being written
     * @param amount the amount of bits to be written
     * @param data th data to be written
     */
    private void updateLocal(BitsSet address, OperandSize amount, BitsSet data) {
        this.sets[this.calculateSetIndex(address)].updateBlock(address, amount,data);
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
            (int) addressInt/this.blockSize % this.sets.length
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
            value.getRealSize()-bitsSetCutIndex, value.getRealSize()
        );
        //---------------------------------------------------------------------
        return result;
        //---------------------------------------------------------------------
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
