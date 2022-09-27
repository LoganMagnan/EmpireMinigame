package org.altopia.empiregame.plugin.runnables;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.customevents.CustomEventType;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameRunnable extends BukkitRunnable {

    private Main main = Main.getInstance();

    @Override
    public void run() {
        if (!this.main.getGameManager().getGame().isStarted()) {
            this.cancel();

            return;
        }

        switch (this.main.getCustomEventManager().getCurrentCustomEvent()) {
            case NONE:
                break;
            case DROUGHT:
                if (this.main.getCustomEventManager().getDuration() <= 0L) {
                    this.main.getCustomEventManager().setCurrentCustomEvent(CustomEventType.NONE);
                    this.main.getCustomEventManager().setDuration(0L);

                    Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.stop.custom-event-stopped")));

                    return;
                }

                this.main.getCustomEventManager().setDuration(this.main.getCustomEventManager().getDuration() - 1L);

                break;
            case MONSOON:
                if (this.main.getCustomEventManager().getDuration() <= 0L) {
                    this.main.getCustomEventManager().setCurrentCustomEvent(CustomEventType.NONE);
                    this.main.getCustomEventManager().setDuration(0L);

                    Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.stop.custom-event-stopped")));

                    return;
                }

                this.main.getCustomEventManager().setDuration(this.main.getCustomEventManager().getDuration() - 1L);

                break;
            case FAMINE:
                if (this.main.getCustomEventManager().getDuration() <= 0L) {
                    this.main.getCustomEventManager().setCurrentCustomEvent(CustomEventType.NONE);
                    this.main.getCustomEventManager().setDuration(0L);

                    Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.stop.custom-event-stopped")));

                    return;
                }

                this.main.getCustomEventManager().setDuration(this.main.getCustomEventManager().getDuration() - 1L);

                for (UUID uuid : this.main.getGameManager().getGame().getPlayers()) {
                    Player player = this.main.getServer().getPlayer(uuid);

                    if (player == null) {
                        continue;
                    }

                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1000000, 0));
                }

                break;
            case ZOMBIE_APOCALYPSE:
                if (this.main.getCustomEventManager().getDuration() <= 0L) {
                    this.main.getCustomEventManager().setCurrentCustomEvent(CustomEventType.NONE);
                    this.main.getCustomEventManager().setDuration(0L);

                    Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.stop.custom-event-stopped")));

                    return;
                }

                this.main.getCustomEventManager().setDuration(this.main.getCustomEventManager().getDuration() - 1L);

                break;
        }
    }
}
