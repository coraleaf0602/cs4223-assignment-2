package cache;

public class CacheBlock {
    private int tag;
    private boolean valid;
    private CacheState state;
    private int[] data; // Simulate the block's data (array of words)


    // Default Block Size: 32 bytes
    // size of word = 4 bytes
    // DEFAULT_BLOCK_WORDS = Default Block Size / size of word = 32 / 4 = 8
    private static final int DEFAULT_BLOCK_WORDS = 8;
    private final int DEFAULT_WORD_SIZE = 4; 

    // Default constructor 
    public CacheBlock() {
        System.out.println("Initialising Cache Blocks");
        this.valid = false;
        this.state = CacheState.INVALID;
        this.data = new int[DEFAULT_BLOCK_WORDS];
    }

    // Constructor that takes in Block size 
    public CacheBlock(int blockSize) { 
        System.out.println("Initialising Cache Blocks of size " + blockSize + " bytes"); 
        this.valid = false; 
        this.state = CacheState.INVALID; 
        this.data = new int[blockSize/DEFAULT_WORD_SIZE]; 
    }

    // constructor with tag and data
    public CacheBlock(int tag, int data, int blockSize) {
        this(blockSize);
        this.tag = tag;
        this.valid = true;
        this.state = CacheState.MODIFIED; // Assuming modified state upon write
        this.data[0] = data; // Store data in the first word (can be extended)
    }

    // Getters
    public int getTag() {
        return this.tag;
    }

    public boolean isValid() {
        return this.valid;
    }

    public CacheState getState() {
        return this.state;
    }

    // Setters
    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setState(CacheState state) {
        this.state = state;
    }

    // Read data from the block at a specific word offset
    public int read(int offset) {
        //Converts offset from byte size to word size 
        offset = offset/DEFAULT_WORD_SIZE;
        if (valid) {
            return data[offset]; // Return the word at the given offset
        } else {
            throw new IllegalStateException("Cache block is not valid!");
        }
    }

    // Write data to the block at a specific word offset
    public void write(int offset, int value) {
        //Converts offset from byte size to word size 
        offset = offset/DEFAULT_WORD_SIZE;
        if (valid) {
            data[offset] = value;
            this.state = CacheState.MODIFIED; // Assume write leads to MODIFIED state
        } else {
            throw new IllegalStateException("Cache block is not valid!");
        }
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "CacheBlock [tag=" + tag + ", valid=" + valid + ", state=" + state + "]";
    }
}
