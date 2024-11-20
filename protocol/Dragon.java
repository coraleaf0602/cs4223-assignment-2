//package protocol;
//
//import cache.Cache;
//import cache.CacheBlock;
//
//public class Dragon implements Protocol {
//    @Override
//    public void readCache(CacheBlock block) {
//
//    }
//
//    @Override
//    public void writeCache(int address, int data, Cache cache) {
//
//    }
//    // receiveBusRd(address):
//
//    // Respond to BusRd, either by providing the data and transitioning to Sm state
//    // or by remaining in Sc if updates are being propagated.
//    // receiveBusUpd(address, data):
//
//    // Update the block with new data from another cache’s write operation, staying
//    // in Sc or transitioning to Sm depending on the system's state.
//    // receiveBusRdX(address):
//
//    // Handle exclusive read requests (BusRdX). Transition from Modified to
//    // Exclusive or Sm state, ensuring no other cache has a valid copy.
//    // flushData(address):
//
//    // Specifically for Dragon, handle the flushing of data to maintain coherence
//    // when a block is being modified or evicted.
//    // evictBlock(address):
//
//    // Ensure that data in the Modified state is correctly handled, either through
//    // updating the memory or by propagating the latest state to other caches.
//
//}
