package cache;

public class CacheBlock {
    private int tag;
    private CacheState state;
    private boolean isValid;
    private boolean isDirty;
    private int[] data; // Simulate the block's data (array of words)

    // Default Block Size: 32 bytes
    // size of word = 4 bytes
    // DEFAULT_BLOCK_WORDS = Default Block Size / size of word = 32 / 4 = 8
    private static final int DEFAULT_BLOCK_WORDS = 8;
    private final int DEFAULT_WORD_SIZE = 4;

    // Default constructor
    public CacheBlock(CacheState state) {
        System.out.println("Initialising Cache Blocks");
        this.isValid = false;
        this.isDirty = false;
        this.data = new int[DEFAULT_BLOCK_WORDS];
        this.state = state;
    }

    // Constructor that takes in Block size and state
    public CacheBlock(int blockSize, CacheState state) {
        System.out.println("Initialising Cache Blocks of size " + blockSize + " bytes");
        this.isValid = false;
        this.isDirty = false;
        this.data = new int[blockSize / DEFAULT_WORD_SIZE];
        this.state = state;
    }

    // constructor with tag and data
    public CacheBlock(int tag, int[] data, int blockSize, CacheState state) {
        this(blockSize, state);
        this.tag = tag;
        this.isValid = true;
        this.isDirty = false; // Assuming modified state upon write
        this.data = data; // Store data in the first word (can be extended)
    }

    // Getters
    public int getTag() {
        return this.tag;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public int[] getData() {
        return this.data;
    }

    public CacheState getState() {
        return this.state;
    }

    // Setters
    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public void setState(CacheState state) {
        this.state = state;
    }

    // Read data from the block
    public int read(int offset) {
        if (isValid) {
            offset /= DEFAULT_WORD_SIZE; // Convert byte offset to word index
            return data[offset];
        } else {
            throw new IllegalStateException("Cache block is not valid!");
        }
    }

    // Write data to the block
    public void write(int offset, int[] value) {
        if (isValid) {
            offset /= DEFAULT_WORD_SIZE; // Convert byte offset to word index
            data = value;
            this.isDirty = true; // Mark block as dirty since it has been modified
        } else {
            throw new IllegalStateException("Cache block is not valid!");
        }
    }


    // Override toString for debugging
    @Override
    public String toString() {
        return "CacheBlock [tag=" + tag + ", valid=" + isValid + ", dirty=" + isDirty + "]";
    }

}
