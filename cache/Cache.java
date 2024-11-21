package cache;

import java.util.ArrayList;

import bus.Bus;
import memory.DRAM;
import protocol.*;

public class Cache {
    private final int MEMORY_SIZE_IN_BYTES = 32;
    private ArrayList<CacheSet> cache;
    private int cacheSize;
    private int blockSize; // in bytes
    private int associativity;
    private int numberOfCacheSets;

    public Cache(int cacheSize, int blockSize, int associativity, Protocol protocol) {
        // debug line
        System.out.println("Initialising Cache...");

        cache = new ArrayList<>();
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.associativity = associativity;
        System.out.println(cacheSize);
        System.out.println(blockSize);
        System.out.println(associativity);
        this.numberOfCacheSets = this.cacheSize / (this.blockSize / this.associativity);

        for (int i = 0; i < numberOfCacheSets; i++) {
            this.cache.add(new CacheSet(this.associativity, this.blockSize, protocol));
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


//    public boolean readToAddress(int address) {
//        CacheAddress addressToRead = this.parseMemoryAddress(address);
//        CacheSet cacheSet = cache.get(addressToRead.getSetIndex());
//        CacheBlock block = cacheSet.getBlockByTag(addressToRead.getTag());
//
//        if (block != null) {
//            // Cache hit
//            System.out.println("Cache hit!");
//            // Update LRU order of block - already done
//            return true;
//        } else {
//            // Cache miss
//            System.out.println("Cache miss. Fetching from memory...");
//            // Fetch block from DRAM - simulate fetching from DRAM using memory address
//            int[] data = dram.readBlock(address);
//            bus.sendDataToCache(MEMORY_SIZE_IN_BYTES); // Simulate 32 bytes transferred over the bus
//            cacheSet.insertBlock(new CacheBlock(addressToRead.getTag(), data, blockSize));
//            return false; // Indicate miss
//        }
//    }
//
//    public int writeToAddress(int address, int[] data) {
//        int isDirty = -1;
//        // Parse the memory address to get the cache-specific information
//        CacheAddress addressToWrite = this.parseMemoryAddress(address);
//        // Get the appropriate cache set based on the set index
//        CacheSet cacheSet = cache.get(addressToWrite.getSetIndex());
//        // Search for the block with the given tag
//        CacheBlock block = cacheSet.getBlockByTag(addressToWrite.getTag());
//        if (block != null) {
//            // Cache hit: Write data to the block
//            System.out.println("Cache hit on write!");
//            block.write(addressToWrite.getBlockOffset(), data);
//            // Update LRU position - already done
//            isDirty = 1;
//            return isDirty;
//        } else {
//            // Cache miss: Fetch the block from memory and insert into the cache
//            System.out.println("Cache miss on write. Fetching block from memory...");
//            isDirty = 0;
//            // Fetch data from memory (simulating the data fetch)
//            int[] fetchedData = dram.readBlock(address);
//            // Simulate 32 bytes of data being transferred over the bus
//            bus.sendDataToCache(MEMORY_SIZE_IN_BYTES);
//            CacheBlock newBlock = new CacheBlock(addressToWrite.getTag(), fetchedData, blockSize);
//            // Insert the new block into the cache set
//            CacheBlock dirtyBlock = cacheSet.insertBlock(newBlock);
//            if (dirtyBlock != null) {
//                dram.writeBlock(address, data);
//                isDirty = 2;
//            }
//            // Now, write to the block that has been inserted
//            newBlock.write(addressToWrite.getBlockOffset(), data);
//            return isDirty;
//        }
//    }

    public CacheBlock findBlock(int address) {
        CacheAddress addressToWrite = this.parseMemoryAddress(address);
        // Get the appropriate cache set based on the set index
        CacheSet cacheSet = cache.get(addressToWrite.getSetIndex());
        // Search for the block with the given tag
        CacheBlock block = cacheSet.getBlockByTag(addressToWrite.getTag());

        // Read/Write miss
        if (block == null) {

        }

        return block;
    }

    public CacheAddress findAddress(int address) {
        // Parse the memory address to get the cache-specific information
        CacheAddress addressToWrite = this.parseMemoryAddress(address);
        return addressToWrite;
    }
}
