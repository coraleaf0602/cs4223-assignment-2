package bus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import cache.CacheController;
import message.*;
import memory.DRAM;

public class Bus {
    private ArrayList<CacheController> caches = new ArrayList<>();
    private Queue<Message> requestMessages = new LinkedList<>();
    private Queue<Message> replyMessages = new LinkedList<>();
    private ArrayList<Integer> invalidateMessages = new ArrayList<>();
    private int numberOfInvalidations = 0;
    private int dataTraffic = 0; // Bytes transferred on the bus
    public DRAM dram;

    public Bus(DRAM dram) {
        this.dram = dram;
    }
    // Increments the total data traffic counter whenever data is sent over the bus
    public void sendDataToCache(int bytes) {
        this.dataTraffic += bytes;
        // System.out.println("Data traffic increased by " + bytes + " bytes.");
    }

    public int getDataTraffic() {
        return this.dataTraffic;
    }

    // Registers a new cache controller with the bus
    public void registerCache(CacheController cache) {
        caches.add(cache);
    }


    // Handles outgoing requests from cache controllers
    public void sendRequest(Message msg) {
        // Handle the request, check the type of request, and forward it to the correct
        // destination
        // Log the request
        System.out.println("Bus received request: " + msg.getType() + " for address: " + msg.getAddress());

        // Propagate the request to all registered cache controllers
        for (CacheController controller : caches) {
            if (controller.getID() != msg.getSenderId()) {
                controller.receiveMessage(msg);
            }
        }
    }

    // Handles incoming request to the bus from cache controller
    public void receiveRequest(Message msg) {
        MessageType type = msg.getType();
        switch(type) {
        case BUS_RD:
            // Receives a request for reading data at a certain address
            // Sends it to other controllers
            for(CacheController controller : caches) {
                if (controller.getID() != msg.getSenderId()) {
                    controller.receiveMessage(msg);
                }
            }
            break;
        case BUS_RDX:
            // Receives a request for exclusive ownership
            // Invalidates the copies in other controllers
            invalidateMessages.clear();
            for(CacheController controller : caches) {
                if(controller.getID() != msg.getSenderId() && controller.hasBlock(msg.getAddress())) {
                    invalidateMessages.add(controller.getID());
                    controller.receiveMessage(msg);
                }
            }

            if(invalidateMessages.isEmpty()) {
                // Fetch from memory and send back to cache if there is no copy
                int[] data = dram.readBlock(msg.getAddress());
                CacheController controller = caches.get(msg.getSenderId());
                controller.receiveMessage(new Message(MessageType.BUS_DATA, msg.getAddress(), msg.getSenderId(), data));
            }
            break;
        case BUS_UPGR:
            break;
        case BUS_INV:
            // Receives a request that the block copies have been invalidated
            // Check if the arraylist is empty
            numberOfInvalidations++;
            if(!invalidateMessages.isEmpty()) {
                invalidateMessages.remove(msg.getSenderId());
                if(invalidateMessages.size() == 1) {
                    int requesterId = invalidateMessages.get(0);
                    for(CacheController controller : caches) {
                        if(controller.getID() == requesterId) {
                            controller.receiveMessage(new Message(MessageType.BUS_UPGR, msg.getAddress(), msg.getSenderId(), msg.getData()));
                        }
                    }
                }
            }
            break;
        case BUS_DATA:
            // Receives a request with data for the requesting cache
            int senderId = msg.getSenderId();
            CacheController controller = caches.get(senderId);
            // Sends back to requesting cache
            controller.receiveMessage(msg);
            break;
        case BUS_WRITEBACK:

            break;
        }
    }

    public void reportStats() {
        System.out.println("Bus data traffic: " + this.getDataTraffic() + " bytes");
        /*
         * 7. Number of invalidations or updates on the bus
         *
         * 8. Distribution of accesses to private data versus shared data (for example,
         * access to modified state is private, while access to shared state is shared
         * data)
         */
        System.out.println("Number of Invalidations/Updates on the bus: " + numberOfInvalidations);
        System.out.println("");
    }
}
