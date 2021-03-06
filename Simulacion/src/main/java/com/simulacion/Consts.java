package com.simulacion;

public class Consts {
    public static final int MEMORY_SIZE = 2048;

    public static final int STACK_SIZE = 128;
    public static final int DATA_SEGMENT_SIZE = 512;

    public static final int WORD_SIZE = 32;

    public static final int HALFWORD_SIZE = 16;

    public static final int BYTE_SIZE = 8;

    public static final int REGISTER_SIZE = 32;

    public static final int REGISTERS_NUMBER = 32;

    
    //-------------------------------------------------------------------------
    // Cache constants
    public static final int BLOCK_SIZE                = 32;
    public static final int INFO_DATA_INDEX           = 0;
    public static final int INFO_LEVEL_INDEX          = 1;
    public static final int INFO_ADDRESS              = 2;
    //-------------------------------------------------------------------------
    // Bus codes
    public static final int CONTROL_LINES_SIZE        = 4;
    public static final int MEM_READING_DONE_CODE     = 1;
    public static final int MEM_READ_QUERY_CODE       = 2;
    public static final int MEM_WRITING_DONE_CODE     = 3;
    public static final int MEM_WRITE_WD_QUERY_CODE   = 4;
    public static final int MEM_WRITE_HW_QUERY_CODE   = 5;
    public static final int MEM_WRITE_BT_QUERY_CODE   = 6;
    //-------------------------------------------------------------------------
    // Memory constants
    public static final int MAT                       = 1024;
    //-------------------------------------------------------------------------

}
