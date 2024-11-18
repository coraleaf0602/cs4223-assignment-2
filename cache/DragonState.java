package cache;

public enum DragonState implements CacheState {
    EXCLUSIVE, SHARED_CLEAN, SHARED_MODIFIED, DIRTY;

    @Override
    public String getStateInfo() {
        return this.name();
    }
}
