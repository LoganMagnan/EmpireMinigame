package org.altopia.empiregame.plugin.managers;

import dev.jcsoftware.jscoreboards.JGlobalMethodBasedScoreboard;
import org.altopia.empiregame.Main;
import org.bukkit.entity.Player;

public class ScoreboardManager {

    private Main main = Main.getInstance();

    private JGlobalMethodBasedScoreboard scoreboard;

    public ScoreboardManager() {
        this.scoreboard = new JGlobalMethodBasedScoreboard();
        this.scoreboard.setTitle(this.main.getMessagesConfig().getConfig().getString("scoreboard.title"));
        this.scoreboard.setLines(this.main.getMessagesConfig().getConfig().getString("scoreboard.lines"));
    }

    public void addPlayerToScoreboard(Player player) {
        this.scoreboard.addPlayer(player);
    }

}