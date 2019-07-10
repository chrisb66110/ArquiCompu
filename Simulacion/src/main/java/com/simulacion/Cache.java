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

    private int memoryHitsNecessary;
    private BitsSet blockFromMemory;

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
                this.getDataLowerCache(address, amount);
            }
            else {
                this.getDataMemory(address);
            }
        } else {
            //System.out.println("Had it in cache " + this.level);
            //-----------------------------------------------------------------
            // reduce the size of the reading if I'm the first cache so the CPU gets only what it requested.
            result = this.level == 1 ?
                    this.trimToOperandSize(result,amount)
                    :
                    this.extractUpperLevelBlock(address,result);
            //-----------------------------------------------------------------
            // Setting the result in the info array
            this.createDataReturnedEvent(result, BitsSet.valueOf(address.toInt()));
            //-----------------------------------------------------------------
        }
    }

    /**
     * Asks the cache below me to give the block for the address being requested
     *
     * @param address the address of memory being requested
     * @param amount the amount of bits being requested
     */
    private void getDataLowerCache(BitsSet address, OperandSize amount){
        //-------------------------------------------------------------
        this.cacheDataReturn = this.rxSubscriber.register(
                CacheDataReturn.class,
                event -> {
                    // Check if the event has my information
                    if (
                            this.level ==
                                    (int) event.info[Consts.INFO_LEVEL_INDEX] &&
                                    ((BitsSet)event.info[Consts.INFO_ADDRESS]).equals(address)
                    ) {
                        // saving the block from lower level
                        this.writeLocal(
                                address,
                                (BitsSet) event.info[Consts.INFO_DATA_INDEX]
                        );
                        // If this is the first level, trim so the CPU gets what it requested
                        BitsSet result = this.level == 1 ?
                                this.trimToOperandSize((BitsSet)event.info[Consts.INFO_DATA_INDEX],amount)
                                :
                                this.extractUpperLevelBlock(address, (BitsSet)event.info[Consts.INFO_DATA_INDEX]);

                        // Creating the event to send the data
                        this.createDataReturnedEvent(
                                result,
                                address
                        );
                        this.cacheDataReturn.unsubscribe();
                    }
                }
        );
        // getting the data from the next caché level
        this.nextCache.getBits(address, amount);
    }

    /**
     * Requests the block of memory where the address being requested is located
     *
     * @param address the address of memory being requested
     */
    private void getDataMemory(BitsSet address){
        //-------------------------------------------------------------
        this.memoryHitsNecessary = this.blockSize / Consts.WORD_SIZE;
        this.blockFromMemory = new BitsSet(this.blockSize);

        // The division of address by block size is done to floor the result since they're ints
        BitsSet startAddress = BitsSet.valueOf((address.toInt()/(this.blockSize/8)) * (this.blockSize/8));
        int startAddressInt = startAddress.toInt();

        this.cacheReadsMemory = this.rxSubscriber.register(
                BusSendsSignal.class,
                event -> {
                    //-----------------------------------------------------
                    // Checking if the reading has already finished.
                    if (
                            Consts.MEM_READING_DONE_CODE ==
                                    this.memoryBus.getControlLines().toInt()
                    ) {
                        this.blockFromMemory.insertBits(memoryHitsNecessary * Consts.WORD_SIZE - 1, memoryBus.getDataLines());
                        --this.memoryHitsNecessary;
                        if(memoryHitsNecessary == 0) {
                            this.writeLocal(address, this.blockFromMemory);
                            this.createDataReturnedEvent(this.extractUpperLevelBlock(address, this.blockFromMemory), address);
                            this.cacheReadsMemory.unsubscribe();
                        }
                        else{
                            BitsSet nextAddress = BitsSet.valueOf(startAddress.toInt() + (this.blockSize/8 - (this.memoryHitsNecessary*4)));
                            this.sendSignalRead(nextAddress);
                        }
                    }
                    //-----------------------------------------------------
                });
        //-------------------------------------------------------------
        this.sendSignalRead(startAddress);
        //-------------------------------------------------------------
    }

    /**
     * Extracts the half block where the data is so that the upper level can handle it
     *
     * @param address the address of the memory requested
     * @param block the block from this level where the address is located
     * @return a BitsSet representing the upper level block
     */
    private BitsSet extractUpperLevelBlock(BitsSet address, BitsSet block) {
        double exactBlockPosition = address.toInt() / (this.blockSize/8.0);
        return exactBlockPosition * 10 % 10 >= 5 ?
                block.get(0, this.blockSize / 2)
                :
                block.get(this.blockSize / 2, this.blockSize);
    }

    /**
     * Sets the control signals on the bus for a word read and sends them
     *
     * @param address the address of memory to be read
     */
    private void sendSignalRead(BitsSet address) {
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
        this.updateLocal(address, amount, data);
        //---------------------------------------------------------------------
        // check if this is the last level caché
        if (this.nextCache != null) {
            this.writeCacheNextLevel(address, amount, data);
        } else {
            this.writeMemory(address, amount, data);
        }
    }

    /**
     * Tells the cache below to write the data
     *
     * @param address address in memmory of the data
     * @param amount the amount of data to be stored
     * @param data the data to be stored
     */
    private void writeCacheNextLevel(BitsSet address, OperandSize amount, BitsSet data){
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
    }

    /**
     * Writes the data to memory
     *
     * @param address the address in memory of where to store the data
     * @param amount the amount of data to be stored
     * @param data the data to be stored
     */
    private void writeMemory(BitsSet address, OperandSize amount, BitsSet data){
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
        // write the changes in the current caché level. 
        this.sets[this.calculateSetIndex(address)].writeBits(address, data);
        //---------------------------------------------------------------------
    }

    /**
     * Method that updates the respective part of the block.
     *
     * @param address the address of memory being written
     * @param amount the amount of bits to be written
     * @param data the data to be written
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
        return (addressInt/(this.blockSize/8) % this.sets.length);
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
        return value.get(
            0, size.size
        );
    }

    /**
     * Asks the sets to set their blocks as invalid and the tells
     * the lower level cache to do the same
     */
    public void setInvalid() {
        for (CacheSet set : this.sets) {
            set.setInvalid();
        }

        if(this.nextCache != null)
            this.nextCache.setInvalid();
    }
    //-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
