package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandQuakeTeam implements CommandExecutor {

    /*
        Player joins to quake game. Works only in prestart faze
        !!!!Command for command block!!!!
        command usage: /quake-join x y z
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);

        Player p = targetP(new Location(Bukkit.getWorld(QuakeConfig.ArcadeWorldName), x, y, z));
        QuakeMain.getInstance().Join(p);
        return false;
    }


    // finds player nearest player to given location
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
