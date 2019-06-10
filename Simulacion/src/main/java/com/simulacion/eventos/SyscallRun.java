package com.simulacion.eventos;

/**
 * Start run Syscall.
 * no info.
 */
public class SyscallRun extends Event {
    public SyscallRun(int cycles, Object[] info) {
        super(cycles, "Start run Syscall", info);
    }
}
