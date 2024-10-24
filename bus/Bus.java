package bus;
import message.*;

public class Bus{

    private int dataTraffic = 0;  // Bytes transferred on the bus

    public void sendDataToCache(int bytes) {
        dataTraffic += bytes;
        System.out.println("Data traffic increased by " + bytes + " bytes.");
    }

    public int getDataTraffic() {

        System.out.println("Need to implement this...");
        return this.dataTraffic;
    }

    // void registerCache(){

    // }

    // void sendRequest(Message msg){

    // }

    // void reply(Message msg){

    // }

    // void propagationRequests(){

    // }

    // void propagationReply(){

    // }
}
