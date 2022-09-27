package org.altopia.empiregame.plugin.commands;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission(Utils.ADMIN_PERMISSION_NODE)) {
                return true;
            }
        }

        if (args.length == 0) {
            for (String string : this.main.getMessagesConfig().getConfig().getStringList("event-command.usage")) {
                sender.sendMessage(Utils.translate(string));
            }
        } else {
            switch (args[0]) {
                case "start":
                    this.main.getCustomEventManager().startCustomEvent(sender, args);

                    break;
                case "stop":
                    this.main.getCustomEventManager().stopCustomEvent(sender);

                    break;
                case "list":
                    this.main.getCustomEventManager().showAllCustomEvents(sender);

                    break;
            }

            return true;
        }

        return true;
    }
}
