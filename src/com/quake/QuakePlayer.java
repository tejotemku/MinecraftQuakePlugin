package com.quake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;

public class QuakePlayer {

    Player player;
    int killStreak;
    int killpoints;

    int getKillpoints()
    {
        return  killpoints;
    }

    int getKillStreak()
    {
        return killStreak;
    }

    QuakePlayer(Player p)
    {
        player = p;
        killStreak = 0;
        killpoints = 0;

    }

    boolean gotHit(Player shotBy)
    {
        player.setHealth(20.0);
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
        {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            return false;
        }

        shotBy.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, 1));


        // punkty i seria zabójstw zabójcy
        if (killStreak > 1) {
            killpoints = (int) (Math.pow(killStreak, 2) * 0.65);
            player.getServer().broadcastMessage(ChatColor.GREEN + shotBy.getName() + ChatColor.WHITE + " ended " + ChatColor.RED + player.getName() + ChatColor.WHITE + "'s killstrike (" + ChatColor.DARK_RED + killStreak + ChatColor.WHITE + " kills) and got " + ChatColor.AQUA + (killpoints + 15) + ChatColor.WHITE + "points!");

        } else
            player.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " was killed by " + ChatColor.GREEN + shotBy.getName());

        killStreak = 0;
        return true;
    }

    void incrementKillStreak()
    {
        killStreak++;

        // killstreak 3+ glow
        if (killStreak> 2) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15, 1));
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.BOLD + "'s killstrike rose to " + ChatColor.RED + killStreak);
        }
    }

    void spawnEquipment()
    {
        // creates special crossbow items
        ItemStack crossbow = new ItemStack(Material.CROSSBOW, 1);
        crossbow.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        crossbow.addEnchantment(Enchantment.QUICK_CHARGE, 3);
        ItemMeta c_i = crossbow.getItemMeta();
        c_i.setDisplayName(ChatColor.GOLD + "Laserowa Dzida");
        c_i.setUnbreakable(true);
        crossbow.setItemMeta(c_i);
        // creates special boots
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta b_i = boots.getItemMeta();
        b_i.setDisplayName(ChatColor.DARK_AQUA + "Papucie Zeskoku");
        b_i.setUnbreakable(true);
        boots.setItemMeta(b_i);
        // creates special fireworks
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "give " + player.getName() + " minecraft:firework_rocket{display:{Name:\"\\\"Wybuchowe szczurki na patyku\\\"\"},Enchantments:[{id:luck_of_the_sea,lvl:1}],Fireworks:{Flight:1,Explosions:[{Type:1,Colors:[7995154,16252672,15615],FadeColors:[16777215]}]}} 128");
        // adds to players inventory
        player.getInventory().addItem(crossbow);
        player.getInventory().setBoots(boots);
        // sets score to 0 so it displays
        Score score = player.getScoreboard().getObjective("QuakeTokens").getScore(player);
        score.setScore(0);

    }

    int addScore(int points)
    {
        Score score = player.getScoreboard().getObjective("QuakeTokens").getScore(player);
        score.setScore(score.getScore() + points);
        return score.getScore();
    }



}
