package org.altopia.redis.region;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PointScheduler implements Listener {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<UUID, Player> playersOnline = new ConcurrentHashMap<>();

    public PointScheduler(){
        schedulePointChecker();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable(){
            @Override
            public void run(){
                if (e.getPlayer().isOnline()){
                    playersOnline.put(e.getPlayer().getUniqueId(),e.getPlayer()); // add only some time later.
                }
            }
        }.runTaskLaterAsynchronously(Main.getInstance(),30L);
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent e){
        playersOnline.remove(e.getPlayer().getUniqueId());
    }

    public void schedulePointChecker(){
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            int serverId = Main.getInstance().getConfig().getInt("server-number");
            Main main = Main.getInstance();
            for (Map.Entry<UUID, Player> entry : playersOnline.entrySet()){
                Player player = entry.getValue();
                Location point = player.getLocation();

                PlayerData uuid = main.getPlayerDataManager().getOrCreate(entry.getKey());
                Location last = uuid.getLastLocation();
                if (last != null){
                    if (last.distance(point) >= 2){
                        uuid.setLastLocation(point);
                        main.getPlayerDataManager().savePlayerData(uuid);
                    }
                }

                if (point.getX() < 0){
                    // player should go to server 0;
                    if (serverId == 0){
                        continue;
                    }
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF("empire1");
                    player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
                } else {
                    // player should go to server 1.
                    if (serverId == 1){
                        continue;
                    }
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF("empire2");
                    player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

}