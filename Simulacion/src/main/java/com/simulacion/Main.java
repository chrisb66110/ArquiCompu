package com.simulacion;

import com.simulacion.eventos.Event;
import com.simulacion.eventos.Halt;

public class Main {

    public static void main(String[] args) {
        RxBus bus = RxBus.getInstance();

        bus.register(Event.class, event -> {
            long num = 0;
            for(int i=0; i < 1000; i++){
                num = num + i % 10;
            }
            System.out.println("numero: " + num);
            bus.post(new Halt(0, 0, null));
        });

        bus.register(Halt.class, event -> {
            System.out.println("test");
        });

        for(int i = 0; i< 10; i++) {
            bus.post(new Event(0,0, "Hola",null));
            System.out.println("Finitio");
        }

        OperatingSystem os = new OperatingSystem();

        os.menu();
    }
}
