package cache;

public class CacheAddress {
    private int blockNumber;
    private int blockOffset;
    private int setIndex;
    private int tag;

    // Constructor
    public CacheAddress(int blockNumber, int blockOffset, int setIndex, int tag) {
        this.blockNumber = blockNumber;
        this.blockOffset = blockOffset;
        this.setIndex = setIndex;
        this.tag = tag;
    }

    // Getters
    public int getBlockNumber() {
        return blockNumber;
    }

    public int getBlockOffset() {
        return blockOffset;
    }

    public int getSetIndex() {
        return setIndex;
    }

    public int getTag() {
        return tag;
    }

    // Override toString for debugging or logging
    @Override
    public String toString() {
        return "CacheAddress [blockNumber=" + blockNumber + ", blockOffset=" + blockOffset +
                ", setIndex=" + setIndex + ", tag=" + tag + "]";
    }
}