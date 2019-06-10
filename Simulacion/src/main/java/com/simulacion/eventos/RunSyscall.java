package com.simulacion.eventos;

/**
 * Start run Syscall
 * no info
 */
public class RunSyscall extends Event {
    public RunSyscall(int cycles, Object[] info) {
        super(cycles, "Start run Syscall", info);
    }
}
