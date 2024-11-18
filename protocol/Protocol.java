package protocol;

import cache.*;

public interface Protocol {
    void readCache(CacheBlock block);

    void writeCache(int address, int data, Cache cache);

    void cacheMiss(int address, boolean isWrite);

    void cacheHit(int address, boolean isWrite);
}
