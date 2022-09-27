package org.altopia.empiregame.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: EmpireMinigame
 */
public abstract class BaseCommand {

    public abstract void executeAs(CommandSender sender, Command cmd, String label, String[] args);

    public abstract List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args);
}
