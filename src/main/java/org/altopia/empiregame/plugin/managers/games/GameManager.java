package org.altopia.empiregame.plugin.managers.games;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.abilities.Ability;
import org.altopia.empiregame.plugin.managers.customevents.CustomEventType;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.playerdata.PlayerState;
import org.altopia.empiregame.plugin.runnables.GameRunnable;
import org.altopia.empiregame.plugin.utils.ItemBuilder;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameManager {

    private Main main = Main.getInstance();

    private Game game;

    public void startGame(CommandSender sender){
        if (this.game == null){
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.no-game-made")));
            return;
        }

        /*if (this.game.isStarted()) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.game-already-started")));

            return;
        }*/

        //this.game.setStarted(true);
        synchronized (this.game.getPlayers()) {
            Iterator<UUID> iterator = this.game.getPlayers().iterator();
            while (iterator.hasNext()){
                UUID uuid = iterator.next();

                Player player = this.main.getServer().getPlayer(uuid);

                if (player == null){
                    iterator.remove();
                    continue;
                }

                player.getInventory().clear();
                player.teleport(this.game.getLocation());

                PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());
                playerData.setPlayerState(PlayerState.GAME);

                this.main.getAbilityManager().getRandomAbility(playerData);

                if (playerData.getAbilityType() == null){
                    player.sendMessage(Utils.translate("&8&m--------------------------------------------"));
                    player.sendMessage(Utils.translate("&dThe game has been started"));
                    player.sendMessage(Utils.translate("&8&m--------------------------------------------"));
                } else {
                    for (Ability ability : this.main.getAbilityManager().getAbilities()){
                        if (ability.getAbilityType() != playerData.getAbilityType()){
                            continue;
                        }

                        player.sendMessage(Utils.translate("&8&m--------------------------------------------"));
                        player.sendMessage(Utils.translate("&dThe game has been started"));
                        player.sendMessage(Utils.translate(""));
                        player.sendMessage(Utils.translate("&9Your ability is &9&l" + ability.getName()));
                        player.sendMessage(Utils.translate("&9Ability description: &9&l" + ability.getDescription()));
                        player.sendMessage(Utils.translate("&8&m--------------------------------------------"));
                    }
                }

                switch (playerData.getAbilityType()) {
                    case THE_JESTER:
                        player.getInventory().setHelmet(new ItemBuilder(Material.GOLDEN_HELMET).amount(1).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchantment(Enchantment.DURABILITY, 3).build());

                        break;
                    case THE_PRECOCIOUS:
                        player.getInventory().addItem(new ItemBuilder(Material.IRON_INGOT).amount(9).build());

                        break;
                    case EVIL_SCIENTIST:
                        player.getInventory().addItem(new ItemBuilder(Material.WITHER_SKELETON_SKULL).amount(1).build());

                        break;
                    case THE_WEALTHY_NOOB:
                        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND).amount(1).build());

                        break;
                    case THE_MINER:
                        player.getInventory().addItem(new ItemBuilder(Material.IRON_PICKAXE).amount(1).build());

                        break;
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getInventory().clear();

                PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

                if (playerData.getPlayerState() == PlayerState.GAME) {
                    continue;
                }

                playerData.setPlayerState(PlayerState.SPECTATOR);

                this.main.getPlayerDataManager().givePlayerItems(player);
            }

            new GameRunnable().runTaskTimerAsynchronously(this.main, 20L, 20L);

            Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.start.game-started")));

        }
    }

    public void stopGame(CommandSender sender) {
        // stop the game
    }

    public void joinGame(CommandSender sender) {
        Player player = (Player) sender;

        if (this.game == null) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.no-game-made")));

            return;
        }

        if (this.game.isStarted()) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.join.game-already-started")));

            return;
        }

        this.game.getPlayers().add(player.getUniqueId());

        player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.join.added-to-the-game")));

        if (this.game.getPlayers().size() > this.game.getMaximumPlayers()) {
            this.startGame(sender);

            return;
        }
    }

    public void loadGame() {
        if (this.main.getSettingsConfig().getConfig().getConfigurationSection("game") != null) {
            List<UUID> players = new ArrayList<UUID>();

            for (String string : this.main.getSettingsConfig().getConfig().getStringList("game.players")) {
                players.add(UUID.fromString(string));
            }

            this.game = new Game(
                    this.main.getServer().getWorld(this.main.getSettingsConfig().getConfig().getString("game.world")),
                    new Location(this.main.getServer().getWorld(this.main.getSettingsConfig().getConfig().getString("game.world")), this.main.getSettingsConfig().getConfig().getDouble("x"), this.main.getSettingsConfig().getConfig().getDouble("y"), this.main.getSettingsConfig().getConfig().getDouble("z")),
                    this.main.getSettingsConfig().getConfig().getInt("game.maximum-players"),
                    players,
                    this.main.getSettingsConfig().getConfig().getBoolean("game.started"),
                    CustomEventType.NONE
            );
        }
    }

    public void saveGame() {
        if (this.game != null) {
            this.main.getSettingsConfig().getConfig().set("game.world", this.game.getWorld().getName());
            this.main.getSettingsConfig().getConfig().set("game.x", this.game.getLocation().getX());
            this.main.getSettingsConfig().getConfig().set("game.y", this.game.getLocation().getY());
            this.main.getSettingsConfig().getConfig().set("game.z", this.game.getLocation().getZ());
            this.main.getSettingsConfig().getConfig().set("game.maximum-players", this.game.getMaximumPlayers());

            List<String> players = new ArrayList<String>();

            for (UUID uuid : this.game.getPlayers()) {
                players.add(uuid.toString());
            }

            this.main.getSettingsConfig().getConfig().set("game.players", players);
            this.main.getSettingsConfig().getConfig().set("game.started", this.game.isStarted());
            this.main.getSettingsConfig().save();
        }
    }

    public void setWorld(CommandSender sender, String[] args) {
        if (this.game == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.no-game-made")));

            return;
        }

        String worldName = args[1];

        if (worldName == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.set-world.world-name-not-valid")));

            return;
        }

        this.game.setWorld(this.main.getServer().getWorld(worldName));

        sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.set-world.world-updated")));
    }

    public void setLocation(CommandSender sender) {
        Player player = (Player) sender;

        if (this.game.getWorld() == null) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.set-location.world-not-valid")));

            return;
        }

        Location location = new Location(this.game.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        this.game.setLocation(location);

        player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.set-location.location-updated")));
    }

    public void setMaximumPlayers(CommandSender sender, String[] args) {
        if (this.game == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.no-game-made")));

            return;
        }

        int maximumPlayers = Integer.parseInt(args[1]);

        this.game.setMaximumPlayers(maximumPlayers);

        sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game-command.set-maximum-players.maximum-players-updated")));
    }

}