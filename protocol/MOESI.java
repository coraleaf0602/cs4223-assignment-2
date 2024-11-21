package protocol;

import cache.*;
import message.*;

public class MOESI implements Protocol {

    @Override
    public int[] readCache(CacheBlock block, int address, CacheController controller) {
        MOESIState state = (MOESIState) block.getState(); // Cast to MESIState
        int[] data = null;
        switch (state) {
            case MODIFIED:
            case OWNER:
            case EXCLUSIVE:
            case SHARED:
                // Return data directly from cache
                data = block.getData();
                break;
            case INVALID:
                // Issue a BusRd request to load the data
                controller.sendMessage(new Message(MessageType.BUS_RD, address, controller.getID()));
                break;
        }
        return data;
    }

    @Override
    public void writeCache(int address, int[] data, Cache cache, CacheController controller) {

    }

    @Override
    public void receiveBusRd(int address, Cache cache, CacheController controller, int senderID) {

    }

    @Override
    public void receiveData(int address, int[] data, Cache cache) {

    }

    @Override
    // Responds to a BusRdX when another processor intends to write to a cache
    public void receiveBusRdX(int address, Cache cache, CacheController controller, int senderID) {

    }

    @Override
    public void receiveBusUpd(int address, int[] data, Cache cache, CacheController controller) {

    }
}
