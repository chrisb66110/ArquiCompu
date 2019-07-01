//-----------------------------------------------------------------------------
// Package
package com.simulacion;
//-----------------------------------------------------------------------------
// imports
import com.simulacion.eventos.MemoryWRDone;
import rx.Subscription;
import com.simulacion.eventos.BusSendsSignal;
import com.simulacion.Consts;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
//-----------------------------------------------------------------------------
/**
 *  Class that emulates the behavoir of a RAM chip.
 * 
 * 
 */
public class Memory {
    //-------------------------------------------------------------------------
    // Global variables
    private BitsSet[] memory;
    private EventHandler eventHandler = EventHandler.getInstance();
    private RxBus rxSubscriber = RxBus.getInstance();
    private Bus bus;
    //--------------------------------------------------------------------------
    // Subscriptions
    private Subscription busSendingData;
    private Subscription memoryFinishedWR;
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
        this.memory = new BitsSet[size];
        Arrays.setAll(this.memory, b -> new BitsSet(8));
        //---------------------------------------------------------------------
        // Setting the bus
        this.bus = bus;
        //---------------------------------------------------------------------
        // creating the subscription to always read the bus signals 
        this.busSendingData = this.rxSubscriber.register(
            BusSendsSignal.class, 
            event ->{
                int controlCode = this.bus.getControlLines().toInt();
                if(Consts.MEM_READING_DONE_CODE != controlCode && Consts.MEM_WRITING_DONE_CODE != controlCode)
                    this.decodeBusSignals();
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
    private void getBits(final int address){
        this.memoryFinishedWR = this.rxSubscriber.register(MemoryWRDone.class, event -> {
            BitsSet[] requestedData = Arrays.copyOfRange(this.memory, address, address + (Consts.BLOCK_SIZE / 8));

            this.createEvent(
                    Consts.MEM_READING_DONE_CODE,
                    //-----------------------------------------------------------------
                    // Reading the data from memory
                    BitsSet.joinBytes(requestedData)
                    //-----------------------------------------------------------------
            );
            this.memoryFinishedWR.unsubscribe();
        });
        //---------------------------------------------------------------------
        this.eventHandler.addEvent(new MemoryWRDone(1024, null));
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
        BitsSet[] requestedData = Arrays.copyOfRange(this.memory, address, address + size.size/8);
        return BitsSet.joinBytes(requestedData);
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
        List<BitsSet> bytes = BitsSet.SplitInBytes(data);

        int i =0;
        for (int index = address; index < address + amount.size / 8; index ++) {
            this.memory[index] = bytes.get(i);
            i++;
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
    private void writeBits(final int address, final int amount, final BitsSet data){
        // Writting the data into memory
        this.memoryFinishedWR = this.rxSubscriber.register(MemoryWRDone.class, event -> {
            List<BitsSet> bytes = BitsSet.SplitInBytes(data);

            int i =0;
            for (int index = address; index < address + amount; index ++) {
                this.memory[index] = bytes.get(i);
                i++;
            }
            //---------------------------------------------------------------------
            // Creating the event to unlock the level above
            this.createEvent(Consts.MEM_WRITING_DONE_CODE, null);
            this.memoryFinishedWR.unsubscribe();
        });

        this.eventHandler.addEvent(new MemoryWRDone(1024, null));
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
            this.bus.sendSignal();
            //-----------------------------------------------------------------
        } catch (Exception e) {
            System.out.println(e);
        }
        //---------------------------------------------------------------------
    }
    //--------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
