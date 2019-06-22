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
        this.memory = new BitsSet(size);
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
        switch(this.bus.getControlLines().toInt()){
            case Consts.MEM_READ_QUERY_CODE:
                //-------------------------------------------------------------
                this.getBits(
                    this.bus.getAddressLines().toInt(),
                    OperandSize.Word
                );
                //-------------------------------------------------------------
                break;
            case Consts.MEM_WRITE_QUERY_CODE:
                //-------------------------------------------------------------
                this.writeBits(
                    this.bus.getAddressLines().toInt(),
                    OperandSize.Word,
                    this.bus.getDataLines()
                );
                break;
                //-------------------------------------------------------------
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
     * @param amount the amount to be fetched
     */
    public void getBits(int address, OperandSize amount){
        //---------------------------------------------------------------------
        this.createEvent(
            Consts.MEM_READING_DONE_CODE,
            //-----------------------------------------------------------------
            // Reading the data from memory
            // TODO: get the amount as a referring number insted of 32
            this.memory.get(address, address + 32) 
            //-----------------------------------------------------------------
        );
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
        // Writting the whole data into memory
        // TODO: use amount to save the changes 
        for (int index = address; index < 32;index ++) {
            this.memory.set(index, data.get(index-address));
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