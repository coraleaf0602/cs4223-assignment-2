package cache;

import java.util.ArrayList;

import bus.Bus;
import memory.DRAM;

public class Cache {

    private ArrayList<CacheSet> cache;
    private int cacheSize;
    private int blockSize; // in bytes
    private int associativity;
    private int numberOfCacheSets;
    // for the outputs
    private DRAM dram;
    private Bus bus;

    public Cache(int cacheSize, int blockSize, int associativity, DRAM dram, Bus bus) {
        // debug line
        System.out.println("Initialising Cache...");

        cache = new ArrayList<>();
        this.dram = dram;
        this.bus = bus;
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.associativity = associativity;
        System.out.println(cacheSize);
        System.out.println(blockSize);
        System.out.println(associativity);
        this.numberOfCacheSets = this.cacheSize / (this.blockSize / this.associativity);

        for (int i = 0; i < numberOfCacheSets; i++) {
            this.cache.add(new CacheSet(this.associativity, this.blockSize));
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

    public boolean readToAddress(int address) {
        CacheAddress addressToRead = this.parseMemoryAddress(address);
        CacheSet cacheSet = cache.get(addressToRead.getSetIndex());
        CacheBlock block = cacheSet.getBlockByTag(addressToRead.getTag());

        if (block != null) {
            // Cache hit
            System.out.println("Cache hit!");
            return true; // Indicate hit
        } else {
            // Cache miss
            System.out.println("Cache miss. Fetching from memory...");
            // Fetch block from DRAM
            // byte[] memoryBlock = dram.readBlock(address);
            bus.sendDataToCache(32); // Simulate 32 bytes transferred over the bus
            // int fetchedData = convertByteArrayToInt(memoryBlock, addressToRead.getBlockOffset());
            // cacheSet.insertBlock(new CacheBlock(addressToRead.getTag(), fetchedData, blockSize));
            return false; // Indicate miss
        }
    }


    public int writeToAddress(int address, int data) {
        // Step 1: Parse the memory address to get the cache-specific information
        CacheAddress addressToWrite = this.parseMemoryAddress(address);

        // Step 2: Get the appropriate cache set based on the set index
        CacheSet cacheSet = cache.get(addressToWrite.getSetIndex());

        // Step 3: Search for the block with the given tag
        CacheBlock block = cacheSet.getBlockByTag(addressToWrite.getTag());

        // Step 4: Handle cache hit or cache miss
        if (block != null) {
            // Cache hit: Write data to the block
            System.out.println("Cache hit on write!");
            block.write(addressToWrite.getBlockOffset(), data);
        } else {
            // Cache miss: Fetch the block from memory and insert into the cache
            System.out.println("Cache miss on write. Fetching block from memory...");

            // Fetch data from memory (simulating the data fetch)
            int fetchedData = fetchFromMemory(address);

            // Create a new cache block with the fetched data and the given tag
            CacheBlock newBlock = new CacheBlock(addressToWrite.getTag(), fetchedData, blockSize);

            // Insert the new block into the cache set
            cacheSet.insertBlock(newBlock);

            // Now, write to the block that has been inserted
            newBlock.write(addressToWrite.getBlockOffset(), data);
        }
        // return the data that was written (for confirmation)
        return data;
    }

    private int fetchFromMemory(int address) {
        // need to do this
        return 0;
    }

    // Helper method to convert bytes from DRAM to int
    private int convertByteArrayToInt(byte[] byteArray, int offset) {
        return byteArray[offset]; // Simplified for demonstration (adjust as needed)
    }
}
