package com.simulacion;

public class ComputerBuilder {
    /**
     * Builds the computer components and associates them
     * @param os The operating system in order to set the references to the computer components
     */
    public static void buildComputer(OperatingSystem os) {
        Bus bus = new Bus(Consts.CONTROL_LINES_SIZE, 32, 32);
        Memory memory = new Memory(Consts.MEMORY_SIZE, bus);
        // Definir las configuraciones de los caches
        Cache L1DataCache = createCacheHierarchy(3, new long[]{64, 128, 256}, new int[]{16, 8, 1}, new int[]{2, 4, 8}, new int[]{32, 64, 128},bus);
        Cache L1InstCache = createCacheHierarchy(3, new long[]{64, 128, 256}, new int[]{16, 8, 1}, new int[]{2, 4, 8}, new int[]{32, 64, 128},bus);
        CPU cpu = new CPU(L1DataCache, L1InstCache);
        EventHandler.getInstance().setCpu(cpu);

        MemoryManager memoryManager = new MemoryManager(memory);
        os.setMemory(memoryManager);
        os.setCpu(cpu);
        os.setInput(new Input());
        os.setOutput(new Output(memoryManager));
        os.setProgramLoader(new ProgramLoader(memoryManager));
    }

    /**
     * Creates the cache hierarchy
     * @param bus the system bus for the last cache
     * @return returns the level 1 cache
     */
    private static Cache createCacheHierarchy(int cacheLevels, long[] cacheSizes, int[] cacheAssociativities, int[] cacheHitTimes, int[] cacheBlockSizes, Bus bus){
        Cache[] caches = new Cache[cacheLevels];
        for(int i = cacheLevels-1; i >= 0; i--) {
            caches[i] = i == 0 ?
                    new Cache(cacheSizes[i], cacheAssociativities[i], i + 1, cacheHitTimes[i], cacheBlockSizes[i],cacheLevels == 1 ? null : caches[i+1], cacheLevels == 1 ? bus : null)
                    :
                    new Cache(cacheSizes[i], cacheAssociativities[i], i + 1, cacheHitTimes[i], cacheBlockSizes[i], i == cacheLevels-1 ? null : caches[i+1], i == cacheLevels-1 ? bus : null);
        }
        return caches[0];
    }
}