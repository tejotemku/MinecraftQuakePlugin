package com.quake;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Library {

    // finds player closest to the given location (coordinates in given world)
    public static Player targetP(Location loc){
        Player nearestPlayer = null;
        double lastDistance = Double.MAX_VALUE;
        for(Player p : loc.getWorld().getPlayers()){
            double distanceSqrd = loc.distanceSquared(p.getLocation());
            if(distanceSqrd < lastDistance){
                lastDistance = distanceSqrd;
                nearestPlayer = p;
            }
        }
        return nearestPlayer;
    }
}
