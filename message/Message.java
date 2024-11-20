package message;

import cache.CacheState;

public class Message {
    private int senderId;  // The ID of the sender, typically a CPU or cache controller
    private int address; // The cache address involved in the message
    private MessageType type;     // The type of message, such as READ_REQ or WRITE_REQ
    private int[] data;
    private CacheState state;

    // Constructor for sending data
    public Message(MessageType type, int address, int senderId, int[] data) {
        this.senderId = senderId;
        this.address = address;
        this.type = type;
        this.data = data;
    }

    // Constructor for no data sent
    public Message(MessageType type, int address, int senderID) {
        this.senderId = senderID;
        this.address = address;
        this.type = type;
    }

    // Getters and Setters
    public int getSenderId() {
        return senderId;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int[] getData() {
        return data;
    }
}