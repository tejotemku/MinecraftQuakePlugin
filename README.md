# Custom Map

Create map of your choosing, but plugin is designed for cubic or spherical maps so you need to define radius as big as map needs in order for plugin to handel it all

In QuakeConig.java define coordinates for lobby, map, radius, game length and more!

# Starting the game

Prepare 3 commandblocks in your lobby. Put in them commands: /quake-prestart, /quake-start and /quake-join

Make sure to put coordinates of pressure plate that activates command block with /quake-join

Example: /quake-join 3 90 -123

Start by stepping on /quake-prestart, then all players join by stepping on /quake-join (!one by one), next /quake-start

# Game Commmands

/quake-start (commandblock command)

/quake-prestart (command block command)

/quake-join x y z (command block command with coordinates as arguments)

/quake-leave (player command)

/jump (commandblock command, example below)

/multiverse x y z world_name(commandblock command, example below)

# Launching pads - jump command

This command's purpose is creating a lunching pad. Put command block, put a block over it,place a pressure plate and insert the command with pressure plate coordinates. Boom! You just step on it and you will be launched in the direction you are looking 
The player that if affected is chosen by shortest distance from command block defined coordinates.

command usage: /jump x y z

![Jump pad Example](https://i.imgur.com/JVpmIE7.png)

# Multiverse command

This command is created to travel between minecraft worlds e.g. if you have normal survival world and quake world and you want to switch between them.

Create a teleport station on spawn, put commandblock and pressure plate, insert command and coordinates and world name.

command usage: /multiverse x y z world_name 
