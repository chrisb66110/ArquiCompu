package com.simulacion;

public class Main {

    public static void main(String[] args) {
        RxBus bus = RxBus.getInstance();

        bus.register(Evento.class, event -> {
           System.out.println("com.simulacion.Evento trigereado");
        });

        bus.post(new Evento());

    }
}
