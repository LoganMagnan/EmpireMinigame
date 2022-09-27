package org.altopia.empiregame;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.plugin.handler.ConnectionHandler;
import org.altopia.empiregame.plugin.listeners.MenuListener;
import org.altopia.empiregame.plugin.listeners.PlayerDataListener;
import org.altopia.empiregame.plugin.listeners.PlayerInteractListener;
import org.altopia.empiregame.plugin.listeners.RandomListeners;
import org.altopia.empiregame.plugin.managers.CommandManager;
import org.altopia.empiregame.plugin.managers.MongoManager;
import org.altopia.empiregame.plugin.managers.PlayerDataManager;
import org.altopia.empiregame.plugin.managers.ScoreboardManager;
import org.altopia.empiregame.plugin.managers.abilities.AbilityManager;
import org.altopia.empiregame.plugin.managers.customevents.CustomEventManager;
import org.altopia.empiregame.plugin.managers.games.GameManager;
import org.altopia.empiregame.plugin.managers.hotbar.HotBarManager;
import org.altopia.empiregame.plugin.menusystem.PlayerMenuUtil;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.utils.Utils;
import org.altopia.empiregame.plugin.utils.config.FileConfig;
import org.altopia.empiregame.plugin.utils.config.file.Config;
import org.altopia.redis.comp.RedisEventManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: EmpireMinigame
 */
@Getter
@Setter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private Config settingsConfig;
    private FileConfig messagesConfig;
    private FileConfig abilitiesConfig;
    private FileConfig customEventsConfig;

    private Utils utils;

    private CommandManager commandManager;
    private MongoManager mongoManager;
    private ScoreboardManager scoreboardManager;
    private PlayerDataManager playerDataManager;
    private GameManager gameManager;
    private HotBarManager hotBarManager;
    private AbilityManager abilityManager;
    private CustomEventManager customEventManager;

    private HashMap<Player, PlayerMenuUtil> playerMenuUtilMap = new HashMap<>();

    public void onEnable(){
        instance = this;

        this.saveDefaultConfig();
        this.settingsConfig = new Config("config", this);
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.abilitiesConfig = new FileConfig(this, "abilities.yml");
        this.customEventsConfig = new FileConfig(this, "customevents.yml");
        this.utils = new Utils();
        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(Utils.translate("&dEmpireGame &7- &av" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(Utils.translate("&7Made by &eTrixkz &7+ &ePedro &7(&fDiscord: LoganM#3465 + Pedro Pagani#9675&7)"));
        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");
        getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
        this.loadManagers();
        this.loadListeners();
        this.loadRunnables();
        this.gameManager.loadGame();

        new RedisEventManager(this);
    }

    public void onDisable() {
        instance = null;

        for (PlayerData playerData : this.playerDataManager.getAllPlayers()) {
            this.playerDataManager.savePlayerData(playerData);
        }

        this.mongoManager.disconnect();
        this.gameManager.saveGame();
    }

    private void loadManagers() {
        this.commandManager = new CommandManager();
        this.mongoManager = new MongoManager();
        this.scoreboardManager = new ScoreboardManager();
        this.playerDataManager = new PlayerDataManager();
        this.gameManager = new GameManager();
        this.hotBarManager = new HotBarManager();
        this.abilityManager = new AbilityManager();
        this.customEventManager = new CustomEventManager();
    }

    private void loadListeners() {
        Arrays.asList(
                new ConnectionHandler(this),
                new RandomListeners(),
                new PlayerInteractListener(),
                new MenuListener(),
                new PlayerDataListener()
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadRunnables() {
        // new Aether(this, new ScoreboardProvider());
    }

    public PlayerMenuUtil getPlayerMenuUtil(Player player) {
        PlayerMenuUtil playerMenuUtil;

        if (playerMenuUtilMap.containsKey(player)) {
            return playerMenuUtilMap.get(player);
        } else {
            playerMenuUtil = new PlayerMenuUtil(player);

            playerMenuUtilMap.put(player, playerMenuUtil);

            return playerMenuUtil;
        }
    }
}