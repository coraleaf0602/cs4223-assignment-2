package memory;

// public class DRAM {
//     package memory;

import java.util.HashMap;

public class DRAM {
    // The size of each memory block
    private static final int BLOCK_SIZE = 32; // Assuming block size of 32 bytes for simplicity

    // Simulated memory using a map to hold data with a block address as key
    private HashMap<Integer, byte[]> memory;

    // Memory access time in cycles
    private int accessTime;

    public DRAM(int size, int accessTime) {
        this.memory = new HashMap<>();
        this.accessTime = accessTime; // Set a realistic DRAM access time, e.g., 100 cycles
    }

    /**
     * Reads a block of data from the memory.
     * @param address The address of the block to read.
     * @return The data block.
     */
    public byte[] readBlock(int address) {
        simulateDelay(); // Simulate DRAM access delay
        return memory.getOrDefault(address, new byte[BLOCK_SIZE]); // Return the block or an empty block if not present
    }

    /**
     * Writes a block of data to the memory.
     * @param address The address of the block to write.
     * @param data The data to write.
     */
    public void writeBlock(int address, byte[] data) {
        assert data.length == BLOCK_SIZE; // Ensure that the data block size is consistent
        simulateDelay(); // Simulate DRAM access delay
        memory.put(address, data); // Store the data in the memory
    }

    /**
     * Simulates the delay caused by DRAM access time.
     */
    private void simulateDelay() {
        try {
            // Simulating the delay using thread sleep which represents the DRAM access time in microseconds
            // This is just symbolic and should ideally be modeled according to your simulation requirements
            Thread.sleep(this.accessTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// }
