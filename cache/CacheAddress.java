package cache;

public class CacheAddress {
    private int blockNumber; 
    private int blockOffset; 
    private int setIndex; 
    private int tag;

    CacheAddress(int blockNumber, int blockOffset, int setIndex, int tag) { 
        this.blockNumber = blockNumber; 
        this.blockOffset = blockOffset; 
        this.setIndex = setIndex; 
        this.tag = tag;
    }
}
