// for protocol, not imp rn

package message;

public class Message {

    int pid;
    int address;
    int data;
    MessageType type;

    public Message(int pid, int address, MessageType type) {
        this.pid = pid;
        this.address = address;
        this.type = type;
    }

    public Message(int pid, int address, MessageType type, int data) {
        this(pid, address, type);
        this.data = data;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
