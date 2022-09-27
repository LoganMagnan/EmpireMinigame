package org.altopia.redis.hand;

import org.altopia.redis.RedisChannel;
import org.altopia.redis.RedisChannels;
import org.altopia.redis.RedisMessenger;
import org.altopia.redis.base.SerializedLocation;
import org.altopia.redis.impl.WorldSyncMessage;
import org.altopia.redis.util.BorderUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class WorldHandler implements Listener {

    @EventHandler
    public void onExplosion(EntityExplodeEvent e){
        List<SerializedLocation> blocks = BorderUtil.filterExplosionAtBorder(e.blockList());
        Location middle = e.getLocation();
        SerializedLocation middleLoc = new SerializedLocation(middle.getBlockX(), middle.getBlockY(), middle.getBlockZ(), middle.getWorld().getName());
        Object[] d = {blocks, middleLoc};
        WorldSyncMessage message = new WorldSyncMessage(d, WorldSyncMessage.SyncType.EXPLOSION);
        RedisMessenger.publish(RedisChannels.WORLD_SYNC.name(), RedisChannel.gson.toJson(message));
    }

}