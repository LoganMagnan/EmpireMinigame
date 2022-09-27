package org.altopia.empiregame.plugin.managers.games;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.altopia.empiregame.plugin.managers.customevents.CustomEventType;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Game {

    private World world;
    private Location location;
    private int maximumPlayers;
    private List<UUID> players;
    private boolean started;
    private CustomEventType customEventType;

}