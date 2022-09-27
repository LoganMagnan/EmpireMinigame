package org.altopia.empiregame.plugin.commands;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.abilities.AbilityType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CropBoostCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!this.main.getAbilityManager().canUseAbility(player, AbilityType.THE_HARVESTER)) {
            return true;
        }

        // i still need to make this ability

        return true;
    }
}
