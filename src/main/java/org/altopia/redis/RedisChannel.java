package org.altopia.redis;

import com.google.gson.Gson;
import org.altopia.redis.base.SerializedLocation;
import org.altopia.redis.impl.DataSwitchMessage;
import org.altopia.redis.impl.TabAddMessage;
import org.altopia.redis.impl.TabRemoveMessage;
import org.altopia.redis.impl.WorldSyncMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import redis.clients.jedis.JedisPubSub;

import java.util.List;

public class RedisChannel extends JedisPubSub {

    public static final Gson gson = new Gson();

    @Override
    public void onMessage(String channel, String message){
        switch (RedisChannels.valueOf(channel)){
            case WORLD_SYNC: {
                WorldSyncMessage worldSyncMessage = gson.fromJson(message, WorldSyncMessage.class);
                switch (worldSyncMessage.getSyncType()){
                    case EXPLOSION:{
                        try {
                            List<SerializedLocation> list = (List<SerializedLocation>) worldSyncMessage.getData()[0];
                            SerializedLocation middleLocation = (SerializedLocation) worldSyncMessage.getData()[1];
                        } catch (Exception ig){
                            ig.printStackTrace();
                        }
                        return;
                    }
                }
                return;
            }
            case CHAT:{
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message));
                return;
            }
            case TAB_REMOVE:{
                TabRemoveMessage tabRemoveMessage = gson.fromJson(message,TabRemoveMessage.class);
                return;
            }
            case TAB_ADD:{
                TabAddMessage tabAddMessage = gson.fromJson(message,TabAddMessage.class);
                return;
            }
            case DATA_SWITCH:{
                DataSwitchMessage dataSwitchMessage = gson.fromJson(message,DataSwitchMessage.class);
                return;
            }
        }
    }

}