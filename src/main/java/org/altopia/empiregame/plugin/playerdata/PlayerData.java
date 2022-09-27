package org.altopia.empiregame.plugin.playerdata;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.PlayerDataManager;
import org.altopia.empiregame.plugin.managers.abilities.AbilityType;
import org.altopia.empiregame.plugin.playerdata.currentgame.PlayerCurrentGameData;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private transient Main main = Main.getInstance();

    private transient PlayerDataManager playerDataManager = this.main.getPlayerDataManager();
    private PlayerState playerState = PlayerState.SPAWN;

    private PlayerSettings playerSettings = new PlayerSettings();
    private PlayerCurrentGameData currentGameData = new PlayerCurrentGameData();

    private Location lastLocation;
    private final UUID uniqueId;
    private boolean loaded;

    private AbilityType abilityType = AbilityType.THE_MINER;
    private boolean abilityAlreadyUsed;

    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.loaded = false;
        this.main.getPlayerDataManager().loadPlayerData(this);
    }

}