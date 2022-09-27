package org.altopia.empiregame.plugin.commands;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.abilities.AbilityType;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!this.main.getAbilityManager().canUseAbility(player, AbilityType.THE_HOMESICK)) {
            return true;
        }

        this.main.getServer().getScheduler().runTaskTimer(this.main, new Runnable() {
            int time = 0;

            @Override
            public void run() {
                if (time == 4) {
                    player.teleport(player.getBedLocation());

                    return;
                }

                this.time++;

                player.sendMessage(Utils.translate("&b" + this.time + "..."));
            }
        }, 0L, 20L);

        return true;
    }
}
