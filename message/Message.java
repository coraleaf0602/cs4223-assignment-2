// for protocol, not imp rn

package message;

import cache.*;

public class Message {
    enum MessageType {
        BUS_RD, BUS_RDX, BUS_UPGR, BUS_INV, BUS_DATA, BUS_WRITEBACK
    }

    int pid;
    int stayOnBus;
    int address;
    int data;
    MessageType type;

}
