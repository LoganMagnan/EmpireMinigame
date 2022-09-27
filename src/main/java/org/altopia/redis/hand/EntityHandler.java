package org.altopia.redis.hand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityHandler implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e){

    }

    @EventHandler
    public void onPDeath(PlayerDeathEvent e){

    }

    /*
    DAMAGE SHOULD BE SYNCED BETWEEN BORDERS, IF A PLAYER HITS THE ENEMY HE SHOULD BE ABLE TO ACTUALLY
     SEE THE PLAYER TAKING DAMAGE IT SHOULD ALMOST LOOK LIKE A REAL (ONE SERVER)
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){

    }

}