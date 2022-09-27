package org.altopia.empiregame.plugin.managers.hotbar;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.utils.ItemUtil;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HotBarManager {

    private Main main = Main.getInstance();

    private List<HotBarItem> spectatorItems = new ArrayList<>();
    private List<HotBarItem> spawnItems = new ArrayList<>();

    public HotBarManager() {
        this.loadSpawnItems();
        this.loadSpectatorItems();
    }

    public void loadSpawnItems() {
        this.spawnItems.add(new HotBarItem(
                ItemUtil.createItem(
                        Material.valueOf(this.main.getMessagesConfig().getConfig().getString("hot-bar.spawn-items.join-the-game.material")), Utils.translate(this.main.getMessagesConfig().getConfig().getString("hot-bar.spawn-items.join-the-game.name")), this.main.getMessagesConfig().getConfig().getInt("hot-bar.spawn-items.join-the-game.amount"), (short) this.main.getMessagesConfig().getConfig().getInt("hot-bar.spawn-items.join-the-game.data")
                ), this.main.getMessagesConfig().getConfig().getInt("hot-bar.spawn-items.join-the-game.slot"), this.main.getMessagesConfig().getConfig().getBoolean("hot-bar.spawn-items.join-the-game.enabled"), ActionType.valueOf(this.main.getMessagesConfig().getConfig().getString("hot-bar.spawn-items.join-the-game.action-type")))
        );
    }

    public void loadSpectatorItems() {
        this.spectatorItems.add(new HotBarItem(
                ItemUtil.createItem(
                        Material.valueOf(this.main.getMessagesConfig().getConfig().getString("hot-bar.spectator-items.player-tracker.material")), Utils.translate(this.main.getMessagesConfig().getConfig().getString("hot-bar.spectator-items.player-tracker.name")), this.main.getMessagesConfig().getConfig().getInt("hot-bar.spectator-items.player-tracker.amount"), (short) this.main.getMessagesConfig().getConfig().getInt("hot-bar.spectator-items.player-tracker.data")
                ), this.main.getMessagesConfig().getConfig().getInt("hot-bar.spectator-items.player-tracker.slot"), this.main.getMessagesConfig().getConfig().getBoolean("hot-bar.spectator-items.player-tracker.enabled"), ActionType.valueOf(this.main.getMessagesConfig().getConfig().getString("hot-bar.spectator-items.player-tracker.action-type")))
        );
    }
}