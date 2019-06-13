package com.simulacion.eventos;

/**
 * Finished Syscall.
 * no info.
 */
public class SyscallExecuted extends Event {
    public SyscallExecuted(int cycles, Object[] info) {
        super(cycles, "Finished Syscall", info);
    }
}
