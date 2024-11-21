package message;

public enum MessageType {
    // Used for a read request where the cache line is not modified.
    BUS_RD("Request to read data from a cache line that is not locally available."),

    // Used for a read request that will lead to a modification (read for ownership).
    BUS_RDX("Request to read and gain exclusive ownership of a cache line."),

    // Used to upgrade the status of a cache line from Shared to Modified without transferring data.
    BUS_UPGR("Upgrade a cache line from Shared state without transferring the data."),

    // Invalidate command used to tell other caches to invalidate their copy of a cache line.
    BUS_INV("Command to invalidate copies of a cache line in other caches."),

    // Used to send data from one cache to another or to respond to a BUS_RD request.
    BUS_DATA("Used to transfer cache line data between caches or in response to a read request."),

    // Indicates that the data is being written back to the main memory or another cache.
    BUS_WRITEBACK("Indicate data is being written back to main memory or another cache.");

    private final String description;

    // Constructor for the enum to set the description
    MessageType(String description) {
        this.description = description;
    }

    // Getter method to obtain the description of each enum value
    public String getDescription() {
        return description;
    }
}
