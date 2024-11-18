package bus;

public class Bus {

    private int dataTraffic = 0; // Bytes transferred on the bus

    public void sendDataToCache(int bytes) {
        this.dataTraffic += bytes;
        // System.out.println("Data traffic increased by " + bytes + " bytes.");
    }

    public int getDataTraffic() {
        return this.dataTraffic;
    }

    // void registerCache(){

    // }

    public void sendRequest(Message msg) {
        // Handle the request, check the type of request, and forward it to the correct
        // destination
    }

    public void receiveRequest(Message msg) {
        // Based on the message, determine if the block is present and respond
        // accordingly
    }

    public void reply(Message msg) {
        // Respond with the result of the request (e.g., data from cache or memory)
    }

    void propagationRequests() {
        // Process all queued requests from caches and propagate them
    }

    void propagationReply() {
        // Send replies to the caches or memory based on the processed requests
    }
}
