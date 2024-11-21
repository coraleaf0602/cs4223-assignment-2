package protocol;

import cache.*;
import message.Message;
import message.MessageType;

public class Dragon implements Protocol {

    @Override
    public int[] readCache(CacheBlock block, int address, CacheController controller) {
        DragonState state = (DragonState) block.getState();
        int[] data = null;
        switch (state) {
        case EXCLUSIVE:
            data = block.getData();
            break;
        case SHARED_CLEAN:
            data = block.getData();
            break;
        case SHARED_MODIFIED:
            data = block.getData();
            break;
        case DIRTY:
            data = block.getData();
            break;
        case INVALID:
            controller.sendMessage(new Message(MessageType.BUS_RD, address, controller.getID()));
            System.out.println("Read Miss: Sending bus message");
            break;
        }
        return data;
    }

    @Override
    public void writeCache(int address, int[] data, Cache cache, CacheController controller) {
        CacheBlock block = cache.findBlock(address);
        DragonState state = (DragonState) block.getState();
        switch (state) {
        case EXCLUSIVE:
            // Write to cache
            block.write(cache.findAddress(address).getBlockOffset(), data);
            // Change to Dirty
            block.setState(DragonState.DIRTY);
            break;
        case SHARED_CLEAN:
            // BusUpd to notify other caches to update their copy
            // State changes to Sm + others change their state to Sm
            controller.sendMessage(new Message(MessageType.BUS_UPGR, address, controller.getID(), data));
            block.setState(DragonState.SHARED_MODIFIED);
            block.write(cache.findAddress(address).getBlockOffset(), data);
            break;
        case SHARED_MODIFIED:
            // Bus Upd to ensure all other caches update
            controller.sendMessage(new Message(MessageType.BUS_UPGR, address, controller.getID(), data));
            block.write(cache.findAddress(address).getBlockOffset(), data);
            // No state change
            break;
        case DIRTY:
            // Direct write
            block.write(cache.findAddress(address).getBlockOffset(), data);
            break;
        case INVALID:
            // Write Miss
            // BusRdX
            // If no valid copies - fetched from memory + set to dirty
            // If valid copies, send copy then invalidate their copy and wb if they were dirty
            controller.sendMessage(new Message(MessageType.BUS_RDX, address, controller.getID(), data));
            System.out.println("Write Miss: Sending bus message");
            // block is dirty then
        }

    }

    @Override
    public void receiveBusRd(int address, Cache cache, CacheController controller, int senderID) {
        // If another cache has the block in M or sm - downgrade to sc
        // The requesting block is in Sc state if another cache provides us, otherwise it's in E
        // Check if we have the block
        CacheBlock block = cache.findBlock(address);
        if (block != null) {
            DragonState state = (DragonState) block.getState();
            // Send data back to bus
            controller.sendMessage(new Message(MessageType.BUS_DATA, address, senderID, block.getData()));
            switch (state) {
            case DIRTY:
                // Write back to main memory
                controller.sendMessage(new Message(MessageType.BUS_WRITEBACK, address, controller.getID(), block.getData()));
                block.setState(DragonState.SHARED_CLEAN);
                System.out.println("BusRd + Cache dirty -> Shared Clean");
                break;
            case SHARED_MODIFIED:
                block.setState(DragonState.SHARED_CLEAN);
                System.out.println("BusRd + Cache Shared Modified -> Shared Clean");
                break;
            case EXCLUSIVE:
                // do nothing
                System.out.println("BusRd + Cache Exclusive");
                break;
            case SHARED_CLEAN:
                System.out.println("Nothing happens on BusRd + shared clean");
                break;
            }
        }
        controller.sendMessage(new Message(MessageType.BUS_DATA, address, senderID, null));
    }

    @Override
    public void receiveData(int address, int[] data, Cache cache) {
        // Read Case: if data is null then change to exclusive
        // Receives the data that it was looking for - this means block in cache is in Invalid State
        CacheBlock block = cache.findBlock(address);

        // This data indicates whether if the data is found or not
        if (data == null || data.length == 0) {
            // If data is not found in other caches
            block.setState(DragonState.EXCLUSIVE);
        } else {
            // If data is found
            block.setState(DragonState.SHARED_CLEAN);
        }

        // I need a way to differentiate read vs write cases
        // Write to block and mark requesting block as dirty
    }

    @Override
    public void receiveBusRdX(int address, Cache cache, CacheController controller, int senderId) {
        // Other caches don't have a copy
        CacheBlock block = cache.findBlock(address);
        if (block.isValid()) {
            if (block.getState() == DragonState.DIRTY) {
                controller.sendMessage(new Message(MessageType.BUS_WRITEBACK, address, senderId, block.getData())); // Write back to memory
            }
            block.setData(null);
            // Going to have to check validity in read and writes
            block.setValid(false);
            controller.sendMessage(new Message(MessageType.BUS_INV, address, senderId, block.getData()));
        }
    }

    @Override
    public void receiveBusUpd(int address, int[] data, Cache cache, CacheController controller) {
        CacheBlock block = cache.findBlock(address);
        if (block.isValid()) {
            DragonState state = (DragonState) block.getState();
            switch (state) {
            case EXCLUSIVE:
                System.out.println("Exclusive + BusUpd should not happen");
                break;
            case SHARED_CLEAN:
                block.write(cache.findAddress(address).getBlockOffset(), data);
                block.setState(DragonState.SHARED_MODIFIED);
                break;
            case SHARED_MODIFIED:
                block.write(cache.findAddress(address).getBlockOffset(), data);
                break;
            case DIRTY:
                System.out.println("Dirty + BusUpd should not happen");
                break;
            }
        }
    }
}
