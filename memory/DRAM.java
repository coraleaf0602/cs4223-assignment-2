package memory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DRAM {
    // Simulated memory using a map to hold data with a block address as key
    private HashMap<Integer, int[]> memory;

    public DRAM(int accessTime) {
        this.memory = new HashMap<>();
    }

    /**
     * Reads an integer value from the memory.
     * @param address The address of the block to read.
     * @return The integer data stored at the address, or 0 if no data is present.
     */
    public int[] readBlock(int address) {
        return memory.getOrDefault(address, new int[]{0}); // Return the integer data or 0 if not present
    }

    /**
     * Writes a block of data to the memory.
     * @param address The address of the block to write.
     * @param data The data to write.
     */
    public void writeBlock(int address, int[] data) {

        memory.put(address, data); // Store the data in the memory
    }
}
