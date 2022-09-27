package org.altopia.empiregame.plugin.playerdata.currentgame;

import lombok.Data;
import org.altopia.empiregame.Main;

@Data
public class PlayerCurrentGameData {

    private transient Main main = Main.getInstance();

}