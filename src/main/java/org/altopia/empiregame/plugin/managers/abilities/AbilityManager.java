package org.altopia.empiregame.plugin.managers.abilities;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.playerdata.PlayerData;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

@Getter
@Setter
public class AbilityManager {

    private Main main = Main.getInstance();

    private List<Ability> abilities = new ArrayList<Ability>();
    private List<Ability> setAbilities = new ArrayList<Ability>();

    public AbilityManager() {
        for (AbilityType abilityType : AbilityType.values()) {
            String name = abilityType.name().toLowerCase().replace("_", "-");

            Ability ability = new Ability(this.main.getAbilitiesConfig().getConfig().getString("abilities." + name + ".name"), this.main.getAbilitiesConfig().getConfig().getString("abilities." + name + ".description"), abilityType);
            Bukkit.getLogger().log(Level.INFO, "Loading ability -> " + ability.getName());
            Bukkit.getLogger().log(Level.INFO, String.valueOf(ability.getAbilityType()));

            this.abilities.add(ability);
        }
    }

    public void getRandomAbility(PlayerData playerData) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try {
            int index = random.nextInt(this.abilities.size());
            Ability ability = this.abilities.get(index);
            Bukkit.getLogger().log(Level.INFO, "Ability for -> " + playerData.getUniqueId() + " " + ability);
            while (ability.getAbilityType() == null) {
                Bukkit.getLogger().log(Level.INFO, "Ability type is NONE.");
                index = random.nextInt(this.abilities.size());
                ability = this.abilities.get(index);
                if (ability != null) {
                    playerData.setAbilityType(ability.getAbilityType());
                }
            }
            while (ability.getName() == null) {
                Bukkit.getLogger().log(Level.INFO, "Ability type is NONE.");
                index = random.nextInt(this.abilities.size());
                ability = this.abilities.get(index);
                if (ability != null && ability.getName() != null) {
                    playerData.setAbilityType(ability.getAbilityType());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean canUseAbility(Player player, AbilityType abilityType) {
        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (!this.main.getGameManager().getGame().isStarted()) {
            return false;
        }

        if (playerData.getAbilityType() != abilityType) {
            return false;
        }

        if (playerData.isAbilityAlreadyUsed()) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("game.ability-already-used")));

            return false;
        }

        playerData.setAbilityAlreadyUsed(true);

        return true;
    }
}
