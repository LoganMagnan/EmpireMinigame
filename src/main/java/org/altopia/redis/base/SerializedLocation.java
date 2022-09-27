package org.altopia.redis.base;

public class SerializedLocation {

    private int x;
    private int y;
    private int z;
    private String worldName;

    public SerializedLocation(int a,int b, int c,String world){
        this.x =a;
        this.y = b;
        this.z = c;
        this.worldName = world;
    }

}
