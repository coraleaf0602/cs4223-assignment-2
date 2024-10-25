package cache;

import java.util.LinkedList;

public class CacheSet {
    private LinkedList<CacheBlock> blocks;  // List of cache blocks in this set
    private int associativity;  // Number of blocks in this set (associativity)

    // Constructor
    public CacheSet(int associativity, int blockSize) {
        System.out.println("Initialising Cache Set...");
        this.associativity = associativity;
        this.blocks = new LinkedList<>();

        // Initialize the cache blocks based on associativity
        for (int i = 0; i < this.associativity; i++) {
            blocks.add(new CacheBlock(blockSize));
        }
    }
    // Get a block by its tag
    public CacheBlock getBlockByTag(int tag) {
        // Iterate through all blocks in this cache set
        for (CacheBlock block : blocks) {
            // Check if the block's tag matches the given tag and if the block is valid
            if (block.isValid() && block.getTag() == tag) {
                // Move block to the front of the list to mark it as the most recently used
                blocks.remove(block);
                blocks.addFirst(block);
                return block;  // Return the block if found
            }
        }
        return null;  // Return null if no matching block is found
    }

    // Method to insert a block into the cache set 
    public CacheBlock insertBlock(CacheBlock newBlock) {
        if (blocks.size() >= this.associativity) {
            // Cache is full, manage LRU and potential dirty block
            CacheBlock lruBlock = blocks.removeLast(); // Remove the least recently used block
            // Insert the new block at the front of the list (most recently used)
            blocks.addFirst(newBlock);
            if (lruBlock.isDirty()) {
                return lruBlock;
            }
        }
        return null;
    }
}

