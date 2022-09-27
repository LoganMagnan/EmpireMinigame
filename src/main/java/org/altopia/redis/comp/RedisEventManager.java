package org.altopia.redis.comp;

import org.altopia.redis.RedisMessenger;
import org.altopia.redis.hand.EntityHandler;
import org.altopia.redis.hand.PlayerHandler;
import org.altopia.redis.hand.WorldHandler;
import org.altopia.redis.region.PointScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisEventManager {

    public RedisEventManager(JavaPlugin it){
        JavaPlugin javaPlugin = it;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new EntityHandler(),javaPlugin);
        pluginManager.registerEvents(new PlayerHandler(),javaPlugin);
        pluginManager.registerEvents(new WorldHandler(),javaPlugin);
        pluginManager.registerEvents(new PointScheduler(),javaPlugin);
        new RedisMessenger();
    }

}