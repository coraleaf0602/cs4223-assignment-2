package protocol;

import cache.*;

public interface Protocol {
    int[] readCache(CacheBlock block, int address, CacheController controller);

    void writeCache(int address, int[] data, Cache cache, CacheController controller);

    void receiveBusRd(int address, Cache cache, CacheController controller, int senderID);

    void receiveData(int address, int[] data, Cache cache);

    void receiveBusRdX(int address, Cache cache, CacheController controller, int senderID);

    void receiveBusUpd(int address, int[] data, Cache cache, CacheController controller);
}

