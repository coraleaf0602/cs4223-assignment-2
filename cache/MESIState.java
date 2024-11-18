package cache;

public enum MESIState implements CacheState {
    MODIFIED, EXCLUSIVE, SHARED, INVALID;

    @Override
    public String getStateInfo() {
        return this.name();
    }
}
