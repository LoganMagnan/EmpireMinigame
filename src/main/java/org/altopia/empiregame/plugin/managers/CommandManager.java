package org.altopia.empiregame.plugin.managers;

import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.commands.*;
import org.altopia.empiregame.plugin.commands.game.GameCommand;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: EmpireMinigame
 */
public class CommandManager {

    private Main main = Main.getInstance();

    public CommandManager() {
        this.registerCommands();
    }

    private void registerCommands() {
        this.main.getCommand("game").setExecutor(new GameCommand());
        this.main.getCommand("event").setExecutor(new EventCommand());
        this.main.getCommand("life").setExecutor(new LifeCommand());
        this.main.getCommand("invis").setExecutor(new InvisCommand());
        this.main.getCommand("bed").setExecutor(new BedCommand());
        this.main.getCommand("strength").setExecutor(new StrengthCommand());
        this.main.getCommand("cropboost").setExecutor(new CropBoostCommand());
    }
}
