package com.quake;

import org.bukkit.Material;

class Coordinates {
    int x;
    int y;
    int z;

    Coordinates(int t_x , int t_y, int t_z)
    {
        x = t_x;
        y = t_y;
        z = t_z;
    }
}


public class QuakeConfig {
    // Game configs~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Game settings~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static int Minutes = 8; // game time
    static int Max_Points = 420; // amount of points after which game finishes
    static int DustRespawnTime = 40; // time in seconds after
    static int Gameisabouttostarttime = 15; // pregame time
    static int PointsForDust = 1;
    static int PointsForKill = 15;
    static int TimeBeforeGlowingPrompt = 120; // seconds
    static int TimeBeforeGlowing = 60; // seconds
    // World settings~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static String ArcadeWorldName = "arcadeland"; // name of world folder in server folder, default called "world"
    static Coordinates ArenaCenterCoordinates = new Coordinates(11, 108, -107); // arena cords
    static Coordinates LobbySpawnCoordinates = new Coordinates(13, 107, -15); // lobby location cords
    static Coordinates SuperSniperNest = new Coordinates(44, 138, -94);
    static int ArenaRadius = 50;
    // Player&Items Spawning settings~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Blocks that QuakeMain code uses to recognise where are special locations on the map
    static Material RespawnBlock = Material.DIAMOND_BLOCK; // QuakeMain.GetRespawns
    static Material DustPointsPlacement = Material.BARRIER; // QuakeMain.GetPointsPlacement
    static Material PowerUpPlacement = Material.EMERALD_BLOCK; // QuakeMain.GetPowerUpsPlacement
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // method that calculates how much additional points player gets while having big kill streak
    static int KillstreakPoints(int streak)
    {
        return (int) (Math.pow(streak, 2) * 0.65);
    }
}


