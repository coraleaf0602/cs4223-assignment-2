package cpu;

import cache.Cache;

public class CPU {
    private Cache cache;
    private int computeCycles = 0;
    private int idleCycles = 0;

    public CPU(Cache cache) {
        this.cache = cache;
    }

    public void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        int type = Integer.parseInt(parts[0]);
        int address = Integer.decode(parts[1]);

        if (type == 0) {
            // Load instruction
            cache.readToAddress(address);
            this.idleCycles += 100; // Add DRAM latency
        } else if (type == 1) {
            // Store instruction
            cache.writeToAddress(address, 99); // Write dummy data
        } else if (type == 2) {
            // Compute cycles
            this.computeCycles += address;
        }
    }

    public void reportStats() {
        System.out.println("Compute cycles: " + computeCycles);
        System.out.println("Idle cycles: " + idleCycles);
        System.out.println("Other data needs to be computed too...");
        // System.out.println("Load/Store instructions: " + loadStoreInstructions);
        // System.out.println("Cache hits: " + cacheHits);
        // System.out.println("Cache misses: " + cacheMisses);
        // System.out.println("Total instructions: " + (computeCycles + idleCycles +
        // loadStoreInstructions));
    }
}
