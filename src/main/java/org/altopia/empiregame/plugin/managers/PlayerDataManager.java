package org.altopia.empiregame.plugin.managers;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.managers.abilities.AbilityType;
import org.altopia.empiregame.plugin.managers.hotbar.HotBarItem;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.playerdata.PlayerState;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

	private final Main main = Main.getInstance();

	@Getter private final ConcurrentHashMap<UUID, PlayerData> players = new ConcurrentHashMap<>();

	public PlayerData getOrCreate(UUID uniqueId) {
		return this.players.computeIfAbsent(uniqueId, PlayerData::new);
	}

	public PlayerData getPlayerData(UUID uniqueId) {
		return this.players.getOrDefault(uniqueId, new PlayerData(uniqueId));
	}

	public Collection<PlayerData> getAllPlayers() {
		return this.players.values();
	}

	public void loadPlayerData(PlayerData playerData) {
		Document document = this.main.getMongoManager().getPlayers().find(Filters.eq("uniqueId", playerData.getUniqueId().toString())).first();

		if (document != null) {
			try {
				playerData.setPlayerState(PlayerState.valueOf(String.valueOf(document.get("player-state"))));
				playerData.setAbilityType(AbilityType.valueOf(String.valueOf(document.get("ability-type"))));
				playerData.setAbilityAlreadyUsed(document.getBoolean("ability-already-used"));
			} catch (Exception exception) {

			}
			if (document.containsKey("location")){
				Location location = Location.deserialize((Map<String, Object>) document.get("location"));
				playerData.setLastLocation(location);
			}
		}

		playerData.setLoaded(true);
	}

	public void savePlayerData(PlayerData playerData) {
		Document document = new Document();
		document.put("uniqueId", playerData.getUniqueId().toString());
		document.put("player-state", playerData.getPlayerState().name());
		document.put("ability-type", playerData.getAbilityType().name());
		document.put("ability-already-used", playerData.isAbilityAlreadyUsed());
		document.put("location", playerData.getLastLocation().serialize());

		this.main.getMongoManager().getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
	}

	public void deletePlayer(UUID uniqueId) {
		this.savePlayerData(getPlayerData(uniqueId));
		this.getPlayers().remove(uniqueId);
	}

	public MongoCursor<Document> getPlayersSorted(String stat, int limit) {
		final Document document = new Document();
		document.put(stat, -1);

		return this.main.getMongoManager().getPlayers().find().sort(document).limit(limit).iterator();
	}


	public void givePlayerItems(Player player) {
		PlayerData playerData = this.getPlayerData(player.getUniqueId());

		player.getInventory().clear();

		if (playerData.getPlayerState() == PlayerState.SPAWN) {
			this.main.getHotBarManager().getSpawnItems().stream().filter(HotBarItem::isEnabled).forEach(hotBarItem -> {
				player.getInventory().setItem(hotBarItem.getSlot(), hotBarItem.getItemStack());
			});
		} else if (playerData.getPlayerState() == PlayerState.SPECTATOR) {
			this.main.getHotBarManager().getSpectatorItems().stream().filter(HotBarItem::isEnabled).forEach(hotBarItem -> {
				player.getInventory().setItem(hotBarItem.getSlot(), hotBarItem.getItemStack());
			});
		}

		player.updateInventory();
	}


	public Map<UUID, PlayerData> getPlayers() {
		return players;
	}

}