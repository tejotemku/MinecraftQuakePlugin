package com.quake;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandQuakePreGame implements CommandExecutor {
    /*
            Prepares a game of quake lobby. Players can now join the game.
            !!!!Command for command block!!!!
            command usage: /quake-prestart
         */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        QuakeMain.getInstance().Pregame();
        return false;
    }

}
