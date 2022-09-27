package org.altopia.empiregame.plugin.listeners;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.playerdata.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDataListener implements Listener {

	private Main main = Main.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (this.main.getPlayerDataManager().getPlayers().containsKey(player.getUniqueId())) {
			return;
		}

		this.main.getScoreboardManager().addPlayerToScoreboard(player);
		this.main.getPlayerDataManager().getPlayers().put(player.getUniqueId(), new PlayerData(player.getUniqueId()));

		PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

		if (playerData.getPlayerState() == PlayerState.SPAWN && this.main.getGameManager().getGame().isStarted()) {
			playerData.setPlayerState(PlayerState.SPECTATOR);
		}

		this.main.getPlayerDataManager().givePlayerItems(player);

		if (playerData.getPlayerState() == PlayerState.SPAWN && !this.main.getGameManager().getGame().isStarted()) {
			player.teleport(player.getWorld().getSpawnLocation());
		}
	}

}