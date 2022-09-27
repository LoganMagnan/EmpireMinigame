package org.altopia.redis.hand;

import me.clip.placeholderapi.PlaceholderAPI;
import org.altopia.empiregame.Main;
import org.altopia.redis.RedisChannels;
import org.altopia.redis.RedisMessenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(PlayerDeathEvent e){
        String message = e.getDeathMessage();
        e.setDeathMessage("");
        RedisMessenger.publish(RedisChannels.CHAT.name(), message);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String message = e.getMessage();
        String format = Main.getInstance().getMessagesConfig().getConfig().getString("chat-format");
        format = format.replace("{player}",player.getName()).replace("{message}",message);
        String m = PlaceholderAPI.setPlaceholders(player,format);
        e.setCancelled(true);
        RedisMessenger.publish(RedisChannels.CHAT.name(), m);
    }

}