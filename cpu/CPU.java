package cpu;

import cache.Cache;
import timer.Timer; 

public class CPU {
    private Cache cache;
    private int computeCycles = 0;
    private int idleCycles = 0;
    private int loadInstructions = 0;
    private int storeInstructions = 0;
    private int cacheHits = 0;
    private int cacheMisses = 0;
    private int dramLatency = 100;
    private int cacheHitLatency = 1; 
    private int DUMMY_DATA = 99;
    private Timer timer = new Timer(); 
    private int totalCycles = 0; 


    public CPU(Cache cache) {
        this.cache = cache;
    }

    public void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        int type = Integer.parseInt(parts[0]);
        int address = Integer.decode(parts[1]);
        timer.tick();
        if (type == 0) {
            // Load instruction
            loadInstructions++;
            boolean hit = cache.readToAddress(address);
            if (!hit) {
                cacheMisses++;
                timer.addCycles(dramLatency);
                this.idleCycles += dramLatency; // Add DRAM latency on cache miss
            } else {
                cacheHits++;
                this.idleCycles += cacheHitLatency; // Only add 1 cycle on cache hit
            }
        } else if (type == 1) {
            // Store instruction
            storeInstructions++;
            int hit = cache.writeToAddress(address, DUMMY_DATA); 
            if(hit == 1 || hit == 2) { 
                cacheMisses++; 
                // Need to know if DRAM latency was called 
                if(hit == 2) { 
                    timer.addCycles(dramLatency);
                    this.idleCycles += dramLatency;
                }
            } else { 
                cacheHits++; // Assuming store always hits the cache
                this.idleCycles += cacheHitLatency; // Only add 1 cycle on cache hit
            }
        } else if (type == 2) {
            // Compute cycles
            this.computeCycles += address;
            timer.addCycles(address);
        }
    }

    public void reportStats() {
        /*
         * 1. Overall Execution Cycle (different core will complete at different cycles;
         * report the maximum value across all cores) for the entire trace as well as
         * execution cycle per core 
         */
        System.out.println(timer.getCurrentCycle());
        System.out.println("Overall Execution Cycle: " + timer.getCurrentCycle()); 
        /* 
         * 2. Number of compute cycles per core. These are the total number of cycles
         * spent processing other instructions between load/store instructions
         */
        System.out.println("Number of Compute Cycles: " + computeCycles);
        /* 
         * 3. Number of load/store instructions per core
         */
        System.out.println("Load Number: " + loadInstructions);
        System.out.println("Store Number: " + storeInstructions);
        /* 
         * 4. Number of idle cycles (these are cycles where the core is waiting for the
         * request to the cache to be completed) per core
         */
        System.out.println("Number of Idle Cycles:" + idleCycles);
        /* 
         * 5. Data cache hit and miss counts for each core
         */
        // Calculate Cache Miss Rate
        int totalLoads = loadInstructions + storeInstructions; // Total load/store attempts
        double cacheMissRate = (double) cacheMisses / totalLoads * 100; // Cache miss rate percentage
        System.out.println("Number of Data Cache Miss Count: " + cacheMisses);
        System.out.printf("Data Cache Miss Rate: %.0f%%\n", cacheMissRate);

        // Calculate Cache Hit Rate 
        double cacheHitRate = (double) cacheHits / totalLoads * 100; // Convert to percentage 
        System.out.println("Number of Data Cache Hit Count: " + cacheHits); 
        System.out.printf("Data Cache Hit Rate: %.0f%%\n", cacheHitRate); 
    } 
}
