package com.simulacion;

import com.simulacion.eventos.BusSendsSignal;

public class Bus {
    private BitsSet controlLines;
    private BitsSet dataLines;
    private BitsSet addressLines;
    private EventHandler eventHandler = EventHandler.getInstance();

    Bus(int controlLines, int dataLines, int addressLines){
        this.controlLines = new BitsSet(controlLines);
        this.dataLines = new BitsSet(dataLines);
        this.addressLines = new BitsSet(addressLines);
    }

    public void sendSignal(){
        eventHandler.addEvent(new BusSendsSignal(5, null));
    }

    public void setControl(BitsSet controlCode) throws Exception {
        if(this.controlLines.length() == controlCode.length())
            this.controlLines = controlCode;
        else
            throw new Exception("La cantidad de bits del control code no concuerda con la cantidad de líneas del bus");
    }

    public void setAddress(BitsSet address) throws Exception {
        if(this.addressLines.length() == address.length())
            this.addressLines = address;
        else
            throw new Exception("La cantidad de bits del address no concuerda con la cantidad de líneas del bus");
    }

    public void setData(BitsSet data) throws Exception {
        if(this.dataLines.length() == data.length())
            this.dataLines = data;
        else
            throw new Exception("La cantidad de bits del control code no concuerda con la cantidad de líneas del bus");
    }

    public BitsSet getControlLines() {
        return controlLines;
    }

    public BitsSet getDataLines() {
        return dataLines;
    }

    public BitsSet getAddressLines() {
        return addressLines;
    }
}
