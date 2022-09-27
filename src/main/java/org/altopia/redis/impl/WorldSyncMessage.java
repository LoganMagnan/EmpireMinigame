package org.altopia.redis.impl;

public class WorldSyncMessage {

    private Object[] data;
    private SyncType syncType;

    public WorldSyncMessage(Object[] x,SyncType sa){
        this.data = x;
        this.syncType = sa;
    }

    public Object[] getData() {
        return data;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public enum SyncType {
        EXPLOSION;
    }

}