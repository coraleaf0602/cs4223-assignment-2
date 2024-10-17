package cache;
import java.io.*;
import java.util.*;

class CacheSimulator {
    enum State {
        MODIFIED, EXCLUSIVE, SHARED, INVALID
    }

    class CacheBlock {
        int tag;
        boolean valid;
        State state;

        CacheBlock() {
            valid = false;
            state = State.INVALID;
        }
    }

    CacheBlock[] cache;
    MainMemory memory = new MainMemory();
    int cacheSize = 1024;  // in bytes
    int blockSize = 16;  // in bytes
    int associativity = 1;  // direct mapped for simplicity

    public CacheSimulator() {
        cache = new CacheBlock[cacheSize / blockSize];
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new CacheBlock();
        }
    }

    private void accessCache(int address, boolean isWrite) {
        int index = (address / blockSize) % cache.length;
        int tag = address / (cacheSize / cache.length);
        CacheBlock block = cache[index];

        if (block.valid && block.tag == tag) {
            // Cache hit scenarios
            switch (block.state) {
                case MODIFIED:
                case EXCLUSIVE:
                    if (isWrite) {
                        block.state = State.MODIFIED;
                    }
                    System.out.println("Cache hit: " + block.state);
                    break;
                case SHARED:
                    if (isWrite) {
                        block.state = State.MODIFIED;
                        invalidateOthers(index);
                    }
                    System.out.println("Cache hit: Shared to " + block.state);
                    break;
            }
        } else {
            // Cache miss scenarios
            if (block.valid && block.state == State.MODIFIED) {
                // Write back if dirty
                memory.writeBackToMemory(block.tag);
            }
            block.tag = tag;
            block.valid = true;
            block.state = isWrite ? State.MODIFIED : State.EXCLUSIVE;
            System.out.println("Cache miss - loading block, new state: " + block.state);
        }
    }

    private void invalidateOthers(int index) {
        // Simulate invalidation of other caches holding this block
        System.out.println("Invalidating other caches for index: " + index);
    }

    public void processTraceFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            int operationType = Integer.parseInt(parts[0]);
            int address = Integer.parseInt(parts[1], 16);

            accessCache(address, operationType == 1);
        }
        scanner.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java CacheSimulator <trace_file_path>");
            return;
        }
        String traceFilePath = args[0];
        CacheSimulator simulator = new CacheSimulator();
        simulator.processTraceFile(traceFilePath);
    }
}

class MainMemory {
    public void writeBackToMemory(int tag) {
        System.out.println("Writing back to memory for tag: " + tag);
    }
}
