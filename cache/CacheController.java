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

    public CacheBlock removeBlock(int address) {
        CacheSet set = cache.findSet(address);
        CacheBlock blockToEvict = set.getLRUBlock();  // Method to get the least recently used block

        if (blockToEvict.isDirty()) {
            flushDataToMemory(blockToEvict, address);  // Ensure data is not lost
        }

        set.get(blockToEvict);  // Physically remove the block from the cache set

        System.out.println("Evicted block with tag: " + blockToEvict.getTag() + " from set: " + setIndex);
        return blockToEvict;  // Return the evicted block, might be useful for testing or logging
    }

    public boolean hasBlock(int address) {
       CacheBlock block = this.cache.findBlock(address);
       return block != null;
    }

    public void flushDataToMemory(CacheBlock block, int address) {
        // Assuming block.getAddress() returns the memory address to write to
        int[] dataToWrite = block.getData();
        // Simulating DRAM write (assuming DRAM.write method exists)
        dram.writeBlock(address, dataToWrite);
    }
}
