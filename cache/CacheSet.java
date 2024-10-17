package cache;

import java.util.ArrayList;

public class CacheSet {
    private int associativity; 
    private ArrayList<CacheBlock> blocks; 
    
    CacheSet(int associativity) { 
        this.associativity = associativity; 
        this.blocks = new ArrayList<>(associativity);
        for (int i = 0; i < this.associativity; i++) {
            blocks.add(new CacheBlock());
        }
    }
}
