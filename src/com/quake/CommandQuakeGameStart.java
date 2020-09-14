package com.quake;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandQuakeGameStart implements CommandExecutor {
    /*
        Starts a game of quake, teleports everyone  to arena and gives items
        !!!!Command for command block!!!!
        command usage: /quake-start
     */


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        QuakeMain.getInstance().GameStart();

        return false;
    }
}
