package org.altopia.empiregame.plugin.commands;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.abilities.AbilityType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!this.main.getAbilityManager().canUseAbility(player, AbilityType.SNEAKY_BOY)) {
            return true;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 0));

        return true;
    }
}
