package cpu;

import cache.Cache;

public class CPU {
    private Cache cache;
    private int computeCycles = 0;
    private int idleCycles = 0;
    private int loadInstructions = 0;
    private int storeInstructions = 0;
    private int cacheHits = 0;
    private int cacheMisses = 0;

    public CPU(Cache cache) {
        this.cache = cache;
    }

    public void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        int type = Integer.parseInt(parts[0]);
        int address = Integer.decode(parts[1]);

        if (type == 0) {
            // Load instruction
            loadInstructions++;
            boolean hit = cache.readToAddress(address);
            if (!hit) {
                cacheMisses++;
                this.idleCycles += 100; // Add DRAM latency on cache miss
            } else {
                cacheHits++;
                this.idleCycles += 1; // Only add 1 cycle on cache hit
            }
        } else if (type == 1) {
            // Store instruction
            storeInstructions++;
            cache.writeToAddress(address, 99); // Write dummy data
            cacheHits++; // Assuming store always hits the cache
        } else if (type == 2) {
            // Compute cycles
            this.computeCycles += address;
        }
    }

    public void reportStats() {
        System.out.println("Finished at cycle " + (computeCycles + idleCycles));
        System.out.println("Compute Cycles Number is " + computeCycles);
        System.out.println("Load Number: " + loadInstructions + " Store Number: " + storeInstructions);
        System.out.println("Idle Cycles Number is " + idleCycles);
        
        // Calculate Cache Miss Rate
        int totalLoads = loadInstructions + storeInstructions; // Total load/store attempts
        double cacheMissRate = (double) cacheMisses / totalLoads * 100; // Cache miss rate percentage
        System.out.printf("Data Cache Miss Rate: %.0f%%\n", cacheMissRate);
    }
}
