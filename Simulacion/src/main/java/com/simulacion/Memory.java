//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// imports
import rx.Subscription;
import com.simulacion.eventos.BusSendsSignal;
import com.simulacion.Consts;
//-----------------------------------------------------------------------------
/**
 *  Class that emulates the behavoir of a RAM chip.
 * 
 * 
 */
public class Memory {
    //-------------------------------------------------------------------------
    // Global variables
    private BitsSet memory;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus rxSubscriber = RxBus.getInstance();
    private Bus bus;
    //--------------------------------------------------------------------------
    // Subscriptions
    private Subscription busSendingData;
    //--------------------------------------------------------------------------
    // Constructor
    /**
     * 
     * Constuctor that uses the given size and the given bus to create the RAM
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param size
     * @param bus
     */
    public Memory(int size, Bus bus) {
        //---------------------------------------------------------------------
        // Creating the memory
        // TODO: Memory as an array of bits or as a bank of them?
        this.memory = new BitsSet(size*8);
        //---------------------------------------------------------------------
        // Setting the bus
        this.bus = bus;
        //---------------------------------------------------------------------
        // creating the subscription to always read the bus signals 
        this.busSendingData = this.rxSubscriber.register(
            BusSendsSignal.class, 
            event ->{
                //-------------------------------------------------------------
                // Decoding the signals sent through the bus
                this.decodeBusSignals();
                //-------------------------------------------------------------
            }
        );
        //---------------------------------------------------------------------
    }
    //--------------------------------------------------------------------------
    // Methods
    /**
     * 
     * Handles the decode process when other component sends a signal through 
     * the bus
     * 
     * @author Joseph Rementería (b55824)
     * 
     */
    private void decodeBusSignals () {
        //---------------------------------------------------------------------
        // Auxiliry variables
        int amount = -1;
        boolean writing = true;
        //---------------------------------------------------------------------
        switch(this.bus.getControlLines().toInt()) {
            case Consts.MEM_READ_QUERY_CODE:
                //-------------------------------------------------------------
                this.getBits(
                    this.bus.getAddressLines().toInt()
                );
                //-------------------------------------------------------------
                writing = false;
                //-------------------------------------------------------------
                break;
            case Consts.MEM_WRITE_BT_QUERY_CODE:
                //-------------------------------------------------------------
                amount = Consts.BYTE_SIZE;
                break;
                //-------------------------------------------------------------
            case Consts.MEM_WRITE_HW_QUERY_CODE:
                //-------------------------------------------------------------
                amount = Consts.HALFWORD_SIZE;
                break;
                //-------------------------------------------------------------
            case Consts.MEM_WRITE_WD_QUERY_CODE:
                //-------------------------------------------------------------
                amount = Consts.WORD_SIZE;
                break;
                //-------------------------------------------------------------
        }
        //---------------------------------------------------------------------
        if (writing) {
        //---------------------------------------------------------------------
            this.writeBits(
                this.bus.getAddressLines().toInt(),
                amount,
                this.bus.getDataLines()
            );   
        //---------------------------------------------------------------------
        }        
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Reads the data in the given address.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address address to read
     */
    private void getBits(int address){
        //---------------------------------------------------------------------
        address *= 8;
        //---------------------------------------------------------------------
        this.createEvent(
            Consts.MEM_READING_DONE_CODE,
            //-----------------------------------------------------------------
            // Reading the data from memory
            this.memory.get(address, address + Consts.BLOCK_SIZE) 
            //-----------------------------------------------------------------
        );
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Reads the data in the given address.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address address to read
     * @param size the amount to be fetched
     */
    public BitsSet getBits(int address, OperandSize size) {
        //---------------------------------------------------------------------
        address *= 8;
        BitsSet result = new BitsSet(size.size);
        //---------------------------------------------------------------------
        result = this.memory.get(address,address + size.size);
        //---------------------------------------------------------------------
        return result;
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Write the given data into the given address.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address where to place the data
     * @param amount the amount of bit to write
     * @param data the info to be written to memory 
     */
    public void writeBits(int address, OperandSize amount, BitsSet data){
        //---------------------------------------------------------------------
        address *= 8;
        //---------------------------------------------------------------------
        // Writting the data into memory
        int initDataIndex = data.size() - amount.size;
        for (int index = address; index < address + amount.size; index ++) {
            this.memory.set(
                index, 
                data.get(initDataIndex + (index - address))
            );
        }
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Write the given data into the given address.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param address where to place the data
     * @param amount the amount of bit to write
     * @param data the info to be written to memory 
     */
    private void writeBits(int address, int amount, BitsSet data){
        //---------------------------------------------------------------------
        address *= 8;
        //---------------------------------------------------------------------
        // Writting the data into memory
        int initDataIndex = data.size() - amount;
        for (int index = address; index < address + amount; index ++) {
            this.memory.set(
                index, 
                data.get(initDataIndex + (index - address))
            );
        }
        //---------------------------------------------------------------------
        // Creating the event to unlock the level above
        this.createEvent(Consts.MEM_WRITING_DONE_CODE, null);
        //---------------------------------------------------------------------
    }
    /**
     * 
     * Creates the evet to unlock the level above.
     * 
     * @author Joseph Rementería (b55824)
     * 
     * @param code the code to be send
     * @param data the data read, null if it was a writting
     */
    private void createEvent(int code, BitsSet data) {
        //---------------------------------------------------------------------
        try {
            //-----------------------------------------------------------------
            // Telling the higher level that the data was saved
            this.bus.setControl(
                new BitsSet(
                    Consts.CONTROL_LINES_SIZE,
                    code
                )
            );
            //-----------------------------------------------------------------
            // Setting the data lines, null if it is a writting
            this.bus.setData(data);
            //-----------------------------------------------------------------
            // Creating the BusSendingSignal event.
            // There is no use for the var info
            this.eventHandler.addEvent(new BusSendsSignal(Consts.MAT,null));
            //-----------------------------------------------------------------
        } catch (Exception e) {

        }
        //---------------------------------------------------------------------
    }
    //--------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------