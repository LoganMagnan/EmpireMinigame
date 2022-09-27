package org.altopia.empiregame.plugin.listeners;

import org.altopia.empiregame.plugin.menusystem.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: EmpireMinigame
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();

        if (inventoryHolder instanceof Menu) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) {
                return;
            }

            Menu menu = (Menu) inventoryHolder;
            menu.handleMenu(event);
        }
    }

}