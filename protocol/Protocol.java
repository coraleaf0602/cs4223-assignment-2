package protocol;

import cache.*;

public interface Protocol {
    int[] readCache(CacheBlock block, int address, CacheController controller);

    void writeCache(int address, int data, Cache cache, CacheController controller);
}
