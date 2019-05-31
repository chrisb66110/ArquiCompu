package com.simulacion;

import com.simulacion.eventos.Evento;

public class Main {

    public static void main(String[] args) {
        RxBus bus = RxBus.getInstance();

        bus.register(Evento.class, event -> {
           System.out.println("com.simulacion.eventos.Evento trigereado");
        });

        bus.post(new Evento());

    }
}
