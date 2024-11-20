package protocol;

import cache.*;
import message.*;

public class MESI implements Protocol {
    // Handle read operations. Check the current state, and if necessary, request
    // the data from the bus (BusRd).

    @Override
    public int[] readCache(CacheBlock block, int address, CacheController controller) {
        MESIState state = (MESIState) block.getState(); // Cast to MESIState
        int[] data = null;
        switch (state) {
            case MODIFIED:
                data = block.getData();
                // Return the data directly from the cache.
                break;
            case EXCLUSIVE:
                // Return the data directly from the cache.
                data = block.getData();
                break;
            case SHARED:
                data = block.getData();
                break;
            case INVALID:
                // The processor must issue a BusRd (Bus Read) operation to read the data
                // from either another cache or from the main memory.
                // Sending request to bus to ask for permission to read
                controller.sendMessage(new Message(MessageType.BUS_RD, address, controller.getID()));
                break;
        }
        return data;
    }

    // Manage write operations. If the block is in Shared (S) or Exclusive (E) state
    // and needs modification,
    // transition to Modified (M). Otherwise, issue a BusRdX to gain ownership.
    @Override
    public void writeCache(int address, int[] data, Cache cache, CacheController controller) {
        CacheAddress addressToWrite = cache.findAddress(address);
        CacheBlock block = cache.findBlock(address);
        MESIState state = (MESIState) block.getState(); // Cast to MESIState
        switch (state) {
            case MODIFIED:
                /*
                 * The processor can write directly to the cache line without any bus action,
                 * as it already has exclusive ownership and the data is up-to-date.
                 * The state remains Modified.
                 */
                block.write(addressToWrite.getBlockOffset(), data);
                break;
            case EXCLUSIVE:
                /*
                 * Since the block is already exclusive to this cache but has not been modified
                 * yet, the processor can change the state to Modified and write to the cache
                 * line directly without notifying the bus.
                 * The state transitions from Exclusive to Modified.
                 */
                block.setState(MESIState.MODIFIED);
                block.write(addressToWrite.getBlockOffset(), data);
                break;
            case SHARED:
                /*
                 * To modify the data, the cache must first gain exclusive access.
                 * The processor issues a BusRdX (Bus Read Exclusive) to invalidate other
                 * copies and transition the state to Modified.
                 * This bus action will notify other caches to invalidate their copies of the
                 * block, ensuring that this cache has exclusive access before it modifies the
                 * data.
                 */
                controller.sendMessage(new Message(MessageType.BUS_RDX, address, controller.getID(), data));
                break;
            case INVALID:
                /*
                 * Similar to handling a write miss, the processor issues a BusRdX to load the
                 * block into the cache with exclusive access, transitioning directly to the
                 * Modified state upon receiving the data.
                 * This action ensures that the cache line is fetched and that all other copies
                 * (if any exist) are invalidated.
                 */
                controller.sendMessage(new Message(MessageType.BUS_RDX, address, controller.getID(), data));
                break;
            default:
        }
    }

    @Override
    public void receiveBusRd(int address, Cache cache, CacheController controller, int senderID) {
    /*
     * Respond to a BusRd broadcast from another processor. Transition from Modified
    to Shared or from Exclusive to Shared, providing the data to the requester.
    * */
        // Check if we have the block
        CacheBlock block = cache.findBlock(address);
        if(block != null) {
            MESIState state = (MESIState) block.getState();
            // Send data back to bus
            controller.sendMessage(new Message(MessageType.BUS_DATA, address, senderID, block.getData()));
            switch(state) {
            case MODIFIED:
                // Write back to main memory
                controller.sendMessage(new Message(MessageType.BUS_WRITEBACK, address, controller.getID(), block.getData()));
                block.setState(MESIState.SHARED);
                System.out.println("BusRd + Cache Modified -> Shared");
                break;
            case EXCLUSIVE:
                block.setState(MESIState.SHARED);
                System.out.println("BusRd + Cache Exclusive -> Shared");
                break;
            case SHARED:
                // do nothing
                System.out.println("BusRd + Cache Shared");
                break;
            }
        }
        controller.sendMessage(new Message(MessageType.BUS_DATA, address, senderID, null));
    }

    // Need some way to get the data originally sent out in the request
    @Override
    public void receiveData(int address, int[] data, Cache cache) {
        // Receives the data that it was looking for - this means block in cache is in Invalid State
        CacheBlock block = cache.findBlock(address);

        // This data indicates whether if the data is found or not
        if(block.getState() == MESIState.INVALID) {
            if(data == null || data.length == 0) {
                block.setState(MESIState.EXCLUSIVE);
            } else {
                block.setState(MESIState.SHARED);
            }
        } else {
            System.out.println("Block was NOT in Invalid State despite requesting for data");
        }
    }

    @Override
    // Responds to a BusRdX when another processor intends to write to a cache
    public void receiveBusRdX(int address, Cache cache, CacheController controller) {
        CacheBlock block = cache.findBlock(address);
        if (block != null) {
            if (block.getState() == MESIState.MODIFIED) {
                controller.sendMessage(new Message(MessageType.BUS_WRITEBACK, address, controller.getID(), block.getData())); // Write back to memory
            }
            block.setState(MESIState.INVALID);
            block.setData(null);
            controller.sendMessage(new Message(MessageType.BUS_INV, address, controller.getID(), block.getData()));
        }
    }

    @Override
    public void receiveBusUpd(int address, int[] data, Cache cache, CacheController controller) {
        // Update the block data during a BusUpd operation, applicable if the cache
        // block is in the Shared state.
        CacheBlock block = cache.findBlock(address);
        if (block != null && block.getState() == MESIState.SHARED) {
            block.setData(data);
            block.setState(MESIState.MODIFIED);
        }
    }
}
