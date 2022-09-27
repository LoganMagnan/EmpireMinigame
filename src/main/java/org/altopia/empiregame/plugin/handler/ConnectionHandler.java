package org.altopia.empiregame.plugin.handler;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.PlayerDataManager;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sun.util.calendar.Gregorian;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class ConnectionHandler implements Listener {

    private final Main main;
    public ConnectionHandler(Main plugin){
        this.main = plugin;
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e){
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }
        int hourOfDay = getCalendar().get(GregorianCalendar.HOUR_OF_DAY);
        int hourEst = main.getConfig().getInt("est-hour-open");
        int minuteOfHour= getCalendar().get(GregorianCalendar.MINUTE);
        int minuteEst = main.getConfig().getInt("est-minute-open");

        if (hourOfDay >= hourEst){

        } else {
            kick(e);
            return;
        }

        if (minuteOfHour >= minuteEst){

        } else {
            kick(e);
            return;
        }
        PlayerData playerData = main.getPlayerDataManager().getOrCreate(e.getUniqueId());
        main.getPlayerDataManager().loadPlayerData(playerData);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerJoinEvent e){
        int hourOfDay = getCalendar().get(GregorianCalendar.HOUR_OF_DAY);
        int hourEst = main.getConfig().getInt("est-hour-open");
        int minuteOfHour= getCalendar().get(GregorianCalendar.MINUTE);
        int minuteEst = main.getConfig().getInt("est-minute-open");
        if (hourOfDay >= hourEst){
        } else {
            kick(e);
            return;
        }
        if (minuteOfHour >= minuteEst){
        } else {
            kick(e);
            return;
        }
        Player p = e.getPlayer();
        PlayerData uuid = main.getPlayerDataManager().getOrCreate(e.getPlayer().getUniqueId());
        Location last = uuid.getLastLocation();
        if (last == null){
            uuid.setLastLocation(p.getLocation());
            main.getPlayerDataManager().savePlayerData(uuid);
            Bukkit.getLogger().log(Level.INFO,"Last location was null.");
            return;
        }
        p.teleport(last);
    }

    public void kick(PlayerJoinEvent e){
        List<String> messages = main.getMessagesConfig().getConfig().getStringList("time-not-opened-yet");
        StringBuilder stringBuilder = new StringBuilder();

        for (String message : messages) {
            stringBuilder.append(ChatColor.translateAlternateColorCodes('&', message)).append("\n");
        }
        Player p = e.getPlayer();
        p.kickPlayer(stringBuilder.toString());
    }

    public void kick(AsyncPlayerPreLoginEvent e){
        List<String> messages = main.getMessagesConfig().getConfig().getStringList("time-not-opened-yet");
        StringBuilder stringBuilder = new StringBuilder();

        for (String message : messages) {
            stringBuilder.append(ChatColor.translateAlternateColorCodes('&', message)).append("\n");
        }
        e.setKickMessage(stringBuilder.toString());
        e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        CompletableFuture.runAsync(()->{
            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();
            PlayerData playerData = main.getPlayerDataManager().getPlayerData(uuid);
            main.getPlayerDataManager().savePlayerData(playerData);
            main.getPlayerDataManager().getPlayers().remove(uuid);
        });
    }

    public GregorianCalendar getCalendar(){
        return new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    }

}