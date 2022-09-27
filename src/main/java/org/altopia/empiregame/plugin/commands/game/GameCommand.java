package org.altopia.empiregame.plugin.commands.game;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                for (String string : this.main.getMessagesConfig().getConfig().getStringList("game-command.usage")) {
                    player.sendMessage(Utils.translate(string));
                }

                return true;
            } else {
                switch (args[0]) {
                    case "join":
                        this.main.getGameManager().joinGame(sender);

                        return true;
                }
            }

            if (!sender.hasPermission(Utils.ADMIN_PERMISSION_NODE)) {
                player.sendMessage(Utils.translate(Utils.NO_PERMISSION_MESSAGE));

                return true;
            }

            switch (args[0]) {
                case "setlocation":
                    this.main.getGameManager().setLocation(sender);

                    break;
                default:
                    break;
            }
        }

        switch (args[0]) {
            case "start":
                this.main.getGameManager().startGame(sender);

                break;
            case "stop":
                this.main.getGameManager().stopGame(sender);

                break;
            case "setworld":
                this.main.getGameManager().setWorld(sender, args);

                break;
            case "setmaximumplayers":
                this.main.getGameManager().setMaximumPlayers(sender, args);

                break;
        }

        return true;
    }
}
