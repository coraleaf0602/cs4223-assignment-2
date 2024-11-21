package cache;

public enum MOESIState implements CacheState {
    MODIFIED, OWNER, EXCLUSIVE, SHARED, INVALID;

    @Override
    public String getStateInfo() {
        return this.name();
    }
}
