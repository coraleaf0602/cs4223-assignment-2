package cache;

import bus.Bus;
import protocol.*;
import message.*;
import memory.DRAM;

public class CacheController {
    private Cache cache;
    private Bus bus;
    private Protocol protocol;
    private int pid;
    private DRAM dram;

    public CacheController(Cache cache, Bus bus, Protocol protocol, int pid, DRAM dram) {
        this.cache = cache;
        this.bus = bus;
        this.protocol = protocol;
        this.pid = pid;
        this.dram = dram;
    }

    public void receiveMessage(Message msg) {
        MessageType type = msg.getType();
        switch(type) {
        case BUS_RD:
            protocol.receiveBusRd(msg.getAddress(), this.cache, this, msg.getSenderId());
            break;
        case BUS_RDX:
            protocol.receiveBusRdX(msg.getAddress(), this.cache, this);
        case BUS_UPGR:
            protocol.receiveBusUpd(msg.getAddress(), msg.getData(), this.cache, this);
        case BUS_DATA:
            protocol.receiveData(msg.getAddress(), msg.getData(), this.cache);
            break;
        }
    }

    public void sendMessage(Message msg) {
        bus.receiveRequest(msg);
    }

    public int getID() {
        return this.pid;
    }

    public void setID(int pid) {
        this.pid = pid;
    }

    public boolean hasBlock(int address) {
       CacheBlock block = this.cache.findBlock(address);
       return block != null;
    }
}
