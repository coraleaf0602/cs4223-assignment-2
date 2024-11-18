package cache;

import bus.Bus;
import protocol.*;

public class CacheController {
    private Cache cache;
    private Bus bus;
    private Protocol protocol;

    public CacheController(Cache cache, Bus bus, Protocol protocol) {
        this.cache = cache;
        this.bus = bus;
        this.protocol = protocol;
    }

    public void readAddress(int address) {
        CacheBlock block = cache.findBlock(address);
        protocol.readCache(block);
    }

    public void writeAddress(int address, int data) {
        protocol.writeCache(address, data, this.cache);
    }

    public void receiveMessage(Message msg) {
        protocol.handleMessage(msg, this);
    }

    public void sendMessage(Message msg) {
        bus.send(msg);
    }
}
