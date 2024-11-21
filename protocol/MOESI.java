package protocol;
import cache.*;
import message.Message;
import message.MessageType;

public class MOESI implements Protocol {
    
    @Override
    public int[] readCache(CacheBlock block, int address, CacheController controller) {
        int[] data = null;
        
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
