package org.altopia.redis.comp;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.altopia.redis.base.ExpiringEntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerGhostManager {

    public static final ConcurrentHashMap<UUID, ExpiringEntityPlayer> hashtableNPCs = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, ExpiringEntityPlayer> integerNPCLookup = new ConcurrentHashMap<>();

    public static void updateNPC(Message state) {
        UUID playerUUID = UUID.fromString(playerMessageMap.get("uuid").asString());

        if (state.parameter().equals("MinecraftPlayerDamage")) {
            MinecraftPlayerDamage.process(state, Bukkit.getPlayer(playerUUID), hashtableNPCs.get(UUID.fromString(playerMessageMap.get("uuidofattacker").asString())));
            return;
        }

        // TODO maybe a better design for this?
        ExpiringEntityPlayer expiringEntityPlayer = hashtableNPCs.get(playerUUID);
        if (expiringEntityPlayer == null){
            expiringEntityPlayer = PlayerGhostManager.createNPC(playerMessageMap.get("username").asString(), playerUUID,
                    new Location(Bukkit.getServer().getWorld(Objects.requireNonNull(state.worldName())),
                            state.position().x(), state.position().y(), state.position().z()));
            ProtocolManager.sendJoinPacket(expiringEntityPlayer.grab());
            hashtableNPCs.put(playerUUID, expiringEntityPlayer);
            integerNPCLookup.put(expiringEntityPlayer.grab().ae(), expiringEntityPlayer);
        }

        EntityPlayer entity = expiringEntityPlayer.grab();

        if (state.parameter().equals("MinecraftPlayerQuit")) {
            ProtocolManager.sendLeavePacket(entity);
            int npcId = hashtableNPCs.get(playerUUID).grab().ae();
            hashtableNPCs.remove(playerUUID);
            integerNPCLookup.remove(npcId);
            return;
        }
        processPacket(state, entity);
    }

    /***
     * This gets the UUID of a player from its entity on the other server
     * @param id - entity id
     * @return - the uuid of the player it's mimicking
     */
    public static UUID getUUIDfromID(int id) {
        UUID uuid = null;
        ExpiringEntityPlayer eep = integerNPCLookup.get(id);
        for (Map.Entry<UUID, ExpiringEntityPlayer> h : hashtableNPCs.entrySet()) {
            if (h.getValue().equals(eep))
                uuid = h.getKey();
        }
        return uuid;
    }


    private static ExpiringEntityPlayer createNPC(String name, UUID uuid, Location location) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(uuid, name);
        EntityPlayer npc = new EntityPlayer(server, world, profile);
        npc.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        return new ExpiringEntityPlayer(npc);
    }

    public static void ensurePlayerHasJoinPackets(UUID p) {
        // Spawn ghosts for this player
        for (Map.Entry<UUID, ExpiringEntityPlayer> uuidExpiringEntityPlayerEntry : hashtableNPCs.entrySet()) {
            ExpiringEntityPlayer expiringEntityPlayer = uuidExpiringEntityPlayerEntry.getValue();
            ProtocolManager.sendJoinPacket(expiringEntityPlayer.grab(), Bukkit.getPlayer(p));
        }
    }

    public static void processPacket(Message state, EntityPlayer entity) {
        switch (state.parameter()) {
            case "MinecraftPlayerMove" -> MinecraftPlayerMove.process(state, entity);
            case "MinecraftPlayerSingleAction" -> MinecraftPlayerSingleAction.process(state, entity);
            case "MinecraftPlayerEquipmentEdit" -> MinecraftPlayerEquipmentEdit.process(state, entity);
            case "MinecraftPlayerShieldUse" -> MinecraftPlayerShieldUse.process(state, entity);
            case "MinecraftPlayerShootBow" -> MinecraftPlayerShootBow.process(state, entity);
        }
    }

}