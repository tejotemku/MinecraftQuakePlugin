package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeaveQuake implements CommandExecutor {

    /*
        Sender leaves a game of quake.
        command usage: /quake-leave
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = Bukkit.getPlayer(sender.getName());
        QuakeMain.getInstance().Leave(p);
        return false;
    }

}
