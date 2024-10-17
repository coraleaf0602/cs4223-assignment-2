package cache; 

public class CacheBlock { 
    int tag; 
    boolean valid; 
    CacheState state; 

    CacheBlock() {
        this.valid = false; 
        this.state = CacheState.INVALID; 
    }
}