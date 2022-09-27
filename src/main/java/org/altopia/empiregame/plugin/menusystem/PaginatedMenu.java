package org.altopia.empiregame.plugin.menusystem;

import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Material;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: EmpireMinigame
 */
public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 45;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);
    }

    public void addMenuBorder() {
        inventory.setItem(45, makeItem(Material.OAK_BUTTON, Utils.translate("&d&lLeft")));
        inventory.setItem(49, makeItem(Material.BARRIER, Utils.translate("&d&lGo back")));
        inventory.setItem(53, makeItem(Material.STONE_BUTTON, Utils.translate("&d&lRight")));

        for (int i = 45; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}