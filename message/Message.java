// for protocol, not imp rn

package message;
import cache.*;
public class Message {

    enum MessageType {
        READ_REQ,
        WRITE_REQ, // for MESI protocol
    };

    int senderID;
    int stayOnBus;
    CacheAddress address;
    MessageType type;

    Message(int senderID, int stayOnBus, CacheAddress address, MessageType type){
        this.senderID = senderID;
        this.stayOnBus = stayOnBus;
        this.address = address;
        this.type = type;
    }
    
}
