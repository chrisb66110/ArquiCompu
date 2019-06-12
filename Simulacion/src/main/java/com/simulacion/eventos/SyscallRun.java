package com.simulacion.eventos;

/**
 * Start run Syscall.
 * 0 -> BitsSet: represents the syscall.
 * 1 -> BitsSet[]: the syscall parameters
 */
public class SyscallRun extends Event {
    public SyscallRun(int cycles, Object[] info) {
        super(cycles, "Start run Syscall", info);
    }
}
