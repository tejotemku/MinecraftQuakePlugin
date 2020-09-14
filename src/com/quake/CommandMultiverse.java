package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandMultiverse implements CommandExecutor
{
    /*
        Command that allows traveling to different worlds if you have multiple
        !!!!Command for command block!!!!
        command usage: /multiverse x y z world_name
     */


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // command arguments
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int z = Integer.parseInt(args[3]);
        World w_current = Bukkit.getWorld(args[4]);
        Player p = targetP(new Location(w_current, x, y, z));

        // Player p = Bukkit.getPlayer(sender.getName());
        World w = Bukkit.getWorld(args[0]);
        p.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
        return false;
    }

    public static Player targetP(Location loc)
    {
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
