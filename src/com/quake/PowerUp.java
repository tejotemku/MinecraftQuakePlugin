package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class PowerUps
{
    public void givePlayerEffect(Player p)
    {

    }
}

class Resistance extends PowerUps
{
    int timeDuration = 20;

    @Override
    public void givePlayerEffect(Player p)
    {
        // add resistance effect that protects player from being hit
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, timeDuration * 20, 1));
    }
}


class SuperRockets extends PowerUps
{
    @Override
    public void givePlayerEffect(Player p)
    {
        // creates special fireworks
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "give " + p.getName() + " minecraft:firework_rocket{display:{Name:\"\\\"Wybuchowe szczurki na patyku\\\"\"},Enchantments:[{id:luck_of_the_sea,lvl:1}],Fireworks:{Flight:1,Explosions:[{Type:2,Colors:[7995154,16252672,15615],FadeColors:[16777215]}]}} 8");

    }
}

class SuperJump extends PowerUps
{
    int powerUpTime = 15; //seconds

    @Override
    public void givePlayerEffect(Player p)
    {
        // gives jump effect
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, powerUpTime*20, 1));
    }
}

class Invisibility extends PowerUps
{
    int powerUpTime = 5; // seconds

    @Override
    public void givePlayerEffect(Player p)
    {
        // gives invisibility effect
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, powerUpTime*20, 1));
    }
}

class SniperNestTeleport extends PowerUps
{

    int powerUpTime = 5; // seconds

    @Override
    public void givePlayerEffect(Player p)
    {
       p.teleport(new Location(Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName), QuakeConfig.SuperSniperNest.x, QuakeConfig.SuperSniperNest.y, QuakeConfig.SuperSniperNest.z));
       p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, powerUpTime*20, 1));
    }
}

class MegaPoints extends PowerUps
{
    int additionalPoints = 15;

    @Override
    public void givePlayerEffect(Player p)
    {
        Score score = p.getScoreboard().getObjective("QuakeTokens").getScore(p);
        score.setScore(score.getScore() + additionalPoints);
    }
}

public class PowerUp
{

    List<PowerUps> powerUps = new ArrayList<>();

    PowerUp()
    {
        powerUps.add(new Resistance());
        powerUps.add(new MegaPoints());
        powerUps.add(new SuperRockets());
        powerUps.add(new SuperJump());
        powerUps.add(new Invisibility());
        powerUps.add(new SniperNestTeleport());

    }

    void drawPowerUp(Player p)
    {
        Random random = new Random();
        powerUps.get(random.nextInt(powerUps.size())).givePlayerEffect(p);
    }

}
