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
    private int dataTraffic = 0; // Bytes transferred on the bus
    private DRAM dram;

    public void Bus(DRAM dram) {
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
                } else {
                    // Add receiver ID (original sender to the head of list)
                    invalidateMessages.add(0, controller.getID());
                }
            }

            // If it's just the original sender
            if(invalidateMessages.size() == 1) {
                // Fetch from memory and send back to cache if there is no copy
                int[] data = dram.readBlock(msg.getAddress());
                int requestorId = invalidateMessages.get(0);
                CacheController controller = caches.get(requestorId);
                if(controller.getID() == msg.getSenderId()) {
                    controller.receiveMessage(new Message(MessageType.BUS_DATA, msg.getAddress(), requestorId, data));
                }
            }
            break;
        case BUS_UPGR:
            break;
        case BUS_INV:
            // Receives a request that the block copies have been invalidated
            // Check if the arraylist is empty
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
            for(CacheController controller : caches) {
                if (controller.getID() == msg.getSenderId()) {
                    // Sends back to requesting cache
                    controller.receiveMessage(msg);
                }
            }
            break;
        case BUS_WRITEBACK:

            break;
        }
    }

    public void reply(Message msg) {
        // Respond with the result of the request (e.g., data from cache or memory)
        CacheController requestingController = getControllerById(msg.getReceiverId());
        if (requestingController != null) {
            requestingController.receiveMessage(msg);
        }
    }
//
//    void propagationReply() {
//        // Send replies to the caches or memory based on the processed requests
//        while (!replyQueue.isEmpty()) {
//            Message msg = replyQueue.poll(); // Dequeue the next reply
//            reply(msg); // Send the reply to the appropriate cache controller
//        }
//    }
//
//    public void propagateRequests() {
//        while (!requestMessages.isEmpty()) {
//            Message msg = requestMessages.poll();
//            boolean[] flags = new boolean[caches.size()];
//            CacheBlock[] cacheBlocks = new CacheBlock[caches.size()];
//
//            if (msg.getBusCycles() == -1) {
//                msg.setBusCycles(TimeConfig.CACHE_HIT);
//                for (int i = 0; i < caches.size(); ++i) {
//                    Cache cache = caches.get(i).getCache();
//                    cacheBlocks[i] = cache.getSet(msg.getAddress().getSetIndex()).isHitMsg(msg.getAddress().getTag());
//                    flags[i] = (cacheBlocks[i] != null);
//                }
//
//                msg.setBusCycles(msg.getBusCycles() + protocol.processMsg(msg, flags, cacheBlocks));
//            }
//            msg.decrementStayInBus();
//            this.sendReply(msg);
//        }
//    }

}
