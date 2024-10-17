package cache;

import java.util.ArrayList; 

public class Cache {
    // private final int CACHE_SIZE_IN_BYTES = 4*1024; 
    // private final int DEFAULT_WORD_SIZE_IN_BYTES = 32/8; 
    // private final int DEFAULT_BLOCK_SIZE_IN_BYTES = 32; 
    // private final int DEFAULT_ASSOCIATIVITY = 2; 

    private ArrayList<CacheSet> cache;
    int cacheSize; 
    int blockSize;  // in bytes
    int associativity;  

    // Cache() { 
    //     int numberOfCacheBlocks = CACHE_SIZE_IN_BYTES/DEFAULT_BLOCK_SIZE_IN_BYTES;
    //     this.cache = new CacheBlock[numberOfCacheBlocks];
    //     for (int i = 0; i < cache.length; i++) {
    //         cache[i] = new CacheBlock();
    //     }
    //     this.wordSize = DEFAULT_WORD_SIZE_IN_BYTES; 
    //     this.blockSize = DEFAULT_BLOCK_SIZE_IN_BYTES;
    //     this.associativity = DEFAULT_ASSOCIATIVITY;
    // }

    Cache(int cacheSize, int blockSize, int associativity) { 
        this.cacheSize = cacheSize; 
        this.blockSize = blockSize; 
        this.associativity = associativity;
        int numberOfCacheSets = this.cacheSize / (this.blockSize / this.associativity);
        
        for (int i = 0; i < numberOfCacheSets; i++) {
            this.cache.add(new CacheSet(this.associativity));
        }
    }

    private void accessCache(int address, boolean isWrite) {
        int index = (address / blockSize) % cache.length;
        int tag = address / (cacheSize / cache.length);
        CacheBlock block = cache[index];

        if (block.valid && block.tag == tag) {
            // Cache hit scenarios
            switch (block.state) {
                case MODIFIED:
                case EXCLUSIVE:
                    if (isWrite) {
                        block.state = State.MODIFIED;
                    }
                    System.out.println("Cache hit: " + block.state);
                    break;
                case SHARED:
                    if (isWrite) {
                        block.state = State.MODIFIED;
                        invalidateOthers(index);
                    }
                    System.out.println("Cache hit: Shared to " + block.state);
                    break;
            }
        } else {
            // Cache miss scenarios
            if (block.valid && block.state == State.MODIFIED) {
                // Write back if dirty
                memory.writeBackToMemory(block.tag);
            }
            block.tag = tag;
            block.valid = true;
            block.state = isWrite ? State.MODIFIED : State.EXCLUSIVE;
            System.out.println("Cache miss - loading block, new state: " + block.state);
        }
    }
}
