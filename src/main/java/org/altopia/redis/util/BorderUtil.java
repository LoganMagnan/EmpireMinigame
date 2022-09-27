package org.altopia.redis.util;

import org.altopia.redis.base.SerializedLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.List;

public class BorderUtil {


    public static List<SerializedLocation> filterExplosionAtBorder(List<Block> blocks){
        List<SerializedLocation> array = new LinkedList<>();

        for (Block block : blocks){
            Location location = block.getLocation();
            if (!checkBorder(location)) continue;
            array.add(new SerializedLocation(location.getBlockX(),location.getBlockY(),location.getBlockZ(),location.getWorld().getName()));
        }

        return array;
    }

    
    public static boolean checkBorder(Location a){
        // condition to check if outside border.
        return false;
    }

}
