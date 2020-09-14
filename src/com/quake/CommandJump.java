package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandJump implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);


        Player player = targetP(new Location(Bukkit.getWorld("arcadeland"), x, y, z));

        player.setVelocity(player.getLocation().getDirection().multiply(3));
        player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));

        return false;
    }

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
