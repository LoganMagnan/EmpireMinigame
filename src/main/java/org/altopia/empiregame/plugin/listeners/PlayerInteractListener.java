package org.altopia.empiregame.plugin.listeners;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.hotbar.HotBarItem;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        if (!event.hasItem()){
            return;
        }

        HotBarItem hotBarItem = Utils.getHotBarItemByItemStack(player.getItemInHand());

        if (hotBarItem == null){
            return;
        }

        if (hotBarItem.getActionType() == null){
            return;
        }

        switch (playerData.getPlayerState()){
            case SPAWN:
                switch (hotBarItem.getActionType()){
                    case GAME_MENU:
                        break;
                }

                break;
            case GAME:
                break;
            case SPECTATOR:
                switch (hotBarItem.getActionType()){
                    case PLAYER_TRACKER_MENU:
                        break;
                }

                break;
        }
    }

}