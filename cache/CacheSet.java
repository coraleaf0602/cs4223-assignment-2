package cache;

import java.util.ArrayList;

public class CacheSet {
    private ArrayList<CacheBlock> blocks;  // List of cache blocks in this set
    private int associativity;  // Number of blocks in this set (associativity)

    // Constructor
    public CacheSet(int associativity) {
        this.associativity = associativity;
        this.blocks = new ArrayList<>();

        // Initialize the cache blocks based on associativity
        for (int i = 0; i < associativity; i++) {
            blocks.add(new CacheBlock());
        }
    }

    // Get a block by its tag
    public CacheBlock getBlockByTag(int tag) {
        // Iterate through all blocks in this cache set
        for (CacheBlock block : blocks) {
            // Check if the block's tag matches the given tag and if the block is valid
            if (block.isValid() && block.getTag() == tag) {
                return block;  // Return the block if found
            }
        }
        return null;  // Return null if no matching block is found
    }

    // Method to insert a block into the cache set (this may need a replacement policy like LRU)
    public void insertBlock(CacheBlock newBlock) {
        // For simplicity, this example just replaces the first invalid block or the first block (could be LRU)
        for (int i = 0; i < blocks.size(); i++) {
            if (!blocks.get(i).isValid()) {
                blocks.set(i, newBlock);  // Replace the invalid block
                return;
            }
        }
        blocks.set(0, newBlock);  // Simple replacement for now
    }
}

