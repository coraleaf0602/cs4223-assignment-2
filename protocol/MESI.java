package protocol;

import cache.*;

public class MESI implements Protocol {
    // Handle read operations. Check the current state, and if necessary, request
    // the data from the bus (BusRd).
    @Override
    public void readCache(CacheBlock block) {
        MESIState state = (MESIState) block.getState(); // Cast to MESIState
        switch (state) {
            case MODIFIED:
                block.getData();
                // Return the data directly from the cache.
                // No bus action is necessary since this processor has the only valid copy of
                // the data.
                break;
            case EXCLUSIVE:
                // Return the data directly from the cache.
                // Again, no bus action is needed because no other cache has a copy of this
                // data.
                block.getData();
                break;
            case SHARED:
                break;
            case INVALID:
                // The processor must issue a BusRd (Bus Read) operation to read the data
                // from either another cache or from the main memory.
                // Depending on whether another cache responds with the data
                // (and whether it was Modified there), this state may transition to Shared
                // (if other caches also hold the data) or Exclusive (if no other cache holds
                // the data).
                break;
            default:
        }
    }

    // Manage write operations. If the block is in Shared (S) or Exclusive (E) state
    // and needs modification,
    // transition to Modified (M). Otherwise, issue a BusRdX to gain ownership.
    @Override
    public void writeCache(int address, int data, Cache cache) {
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
                break;
            case INVALID:
                /*
                 * Similar to handling a write miss, the processor issues a BusRdX to load the
                 * block into the cache with exclusive access, transitioning directly to the
                 * Modified state upon receiving the data.
                 * This action ensures that the cache line is fetched and that all other copies
                 * (if any exist) are invalidated.
                 */
                break;
            default:
        }
    }

    // receiveBusRd(address):

    // Respond to a BusRd broadcast from another processor. Transition from Modified
    // to
    // Shared or from Exclusive to Shared, providing the data to the requester.
    // receiveBusRdX(address):

    // Respond to a BusRdX when another processor intends to write. If in Modified
    // or Exclusive
    // state, flush the data to memory and transition to Invalid.
    // receiveBusUpd(address, data):

    // Update the block data during a BusUpd operation, applicable if the cache
    // block is in
    // the Shared state.
    // evictBlock(address):

    // Manage eviction of a cache block, ensuring that data in the Modified state is
    // written back
    // to memory before eviction.
}
