package cache;

import java.util.ArrayList; 

public class Cache {
    // private final int CACHE_SIZE_IN_BYTES = 4*1024; 
    // private final int DEFAULT_WORD_SIZE_IN_BYTES = 32/8; 
    // private final int DEFAULT_BLOCK_SIZE_IN_BYTES = 32; 
    // private final int DEFAULT_ASSOCIATIVITY = 2; 

    private ArrayList<CacheSet> cache;
    private int cacheSize; 
    private int blockSize;  // in bytes
    private int associativity;  
    private int numberOfCacheSets; 

    // Cache() { 
    //    int numberOfCacheBlocks = CACHE_SIZE_IN_BYTES/DEFAULT_BLOCK_SIZE_IN_BYTES;
    //     this.cache = new CacheBlock[numberOfCacheBlocks];
    //     for (int i = 0; i < cache.length; i++) {
    //         cache[i] = new CacheBlock();
    //     }
    //     this.wordSize = DEFAULT_WORD_SIZE_IN_BYTES; 
    //     this.blockSize = DEFAULT_BLOCK_SIZE_IN_BYTES;
    //     this.associativity = DEFAULT_ASSOCIATIVITY;
    // }/ 

    Cache(int cacheSize, int blockSize, int associativity) { 
        this.cacheSize = cacheSize; 
        this.blockSize = blockSize; 
        this.associativity = associativity;
        this.numberOfCacheSets = this.cacheSize / (this.blockSize / this.associativity);
        
        for (int i = 0; i < numberOfCacheSets; i++) {
            this.cache.add(new CacheSet(this.associativity));
        }
    }


    // convert memory address to cache-specific address 
    public CacheAddress parseMemoryAddress(int address) { 
        int blockNumber = address / this.blockSize; 
        int blockOffset = address % this.blockSize; 
        int setIndex = blockNumber % this.numberOfCacheSets; 
        int tag = blockNumber / this.numberOfCacheSets;
        // Optionally print out the results for verification
        System.out.println("Block Number: " + blockNumber);
        System.out.println("Block Offset: " + blockOffset);
        System.out.println("Set Index: " + setIndex);
        System.out.println("Tag: " + tag);

        return new CacheAddress(blockNumber, blockOffset, setIndex, tag);
    }

    public int readToAddress(int address) { 
        CacheAddress addressToRead = this.parseMemoryAddress(address);

    }

    public int writeToAddress(int address) { 
        CacheAddress addressToWrite = this.parseMemoryAddress(address);
    }

}
