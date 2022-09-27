package org.altopia.empiregame.plugin.managers.hotbar;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HotBarItem {

    private Main main = Main.getInstance();

    private ItemStack itemStack;
    private int slot;
    private boolean enabled;
    private ActionType actionType;

    private static List<HotBarItem> hotBarItems = new ArrayList<>();

    public HotBarItem(ItemStack itemStack, int slot, boolean enabled, ActionType actionType) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.enabled = enabled;
        this.actionType = actionType;

        hotBarItems.add(this);
    }

    public static List<HotBarItem> getHotBarItems() {
        return hotBarItems;
    }
}
