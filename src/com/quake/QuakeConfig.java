package com.quake;

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
    //Game configs~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //Game settings~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static int Minutes = 1; //game time
    static int Max_Points = 420;
    static int DustRespawnTime = 40;
    static int Gameisabouttostarttime = 15;
    static int PointsForDust = 1;
    static int PointsForKill = 15;
    static int TimeBeforeGlowingPrompt = 120; //seconds
    static int TimeBeforeGlowing = 60; //seconds
    //World settings~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static String ArcadeWorldName = "arcadeland";
    static Coordinates ArenaCenterCoordinates = new Coordinates(11, 108, -107);
    static Coordinates LobbySpawnCoordinates = new Coordinates(13, 107, -15);
    static Coordinates SuperSniperNest = new Coordinates(44, 138, -94);
    static int ArenaRadius = 50;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}