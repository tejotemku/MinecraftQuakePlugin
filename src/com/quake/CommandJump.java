package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandJump implements CommandExecutor {
    /*
        This command's purpose is creating a lunching pad. Put command block, put a block over it,place a pressure
        plate and insert the command with pressure plate coordinates. Boom! You just step on it and you will be launched in the direction you are looking
        The player that if affected is chosen by shortest distance from command block.
        !!!!Command for command block!!!!
        command usage: /jump x y z
     */
    // how fast&high you wanna jump
    static int JumpVelocity = 3;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // pressure plate coordinates
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);


        // find closest player
        Player player = Library.targetP(new Location(Bukkit.getWorld(QuakeConfig.ArcadeWorldName), x, y, z));
        // set launching velocity
        player.setVelocity(player.getLocation().getDirection().multiply(JumpVelocity));
        player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
        return false;
    }
}
