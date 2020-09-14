package com.quake;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.*;

public class QuakeMain extends JavaPlugin implements Listener {


    private static QuakeMain _instance;
    public static QuakeMain getInstance()
    {
        return _instance;
    }

    public int game_ID = 0;
    boolean gameIsLive = false;
    boolean pregameTime = false;

    // players
    public List<Player> players = new ArrayList<>();
    public List<Player> playersAlive = new ArrayList<>();
    public Map<Player, QuakePlayer> quakePlayers = new HashMap<>();
    // special locations
    public List<Location> respawns = new ArrayList<>();
    public List<Location> pointsLoc = new ArrayList<>();
    public List<Location> powerUpLoc = new ArrayList<>();


    BossBar infoBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
    ScoreboardManager manager;
    Scoreboard scoreboard;
    Objective obj;
    PowerUp powerUp = new PowerUp();


    @Override
    public void onEnable()
    {

        _instance = this;
        gameIsLive = false;
        pregameTime = false;
        players.clear();
        playersAlive.clear();

        Bukkit.setDefaultGameMode(GameMode.ADVENTURE);
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("quake-start").setExecutor(new CommandQuakeGameStart());
        getCommand("quake-join").setExecutor(new CommandQuakeTeam());
        getCommand("quake-prestart").setExecutor(new CommandQuakePreGame());
        getCommand("quake-leave").setExecutor(new CommandLeaveQuake());
        getCommand("jump").setExecutor(new CommandJump());
        getCommand("resp").setExecutor(new CommandResp());
        getCommand("multiverse").setExecutor(new CommandMultiverse());
        super.onEnable();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }

    public void Pregame()
    {
        if (!pregameTime) {
            infoBar.setVisible(true);
            ChangeBarName("Quake lobby is ready");
            players.clear();
            playersAlive.clear();
            Bukkit.broadcastMessage("Game lobby is ready!");
            manager = Bukkit.getScoreboardManager();
            scoreboard = manager.getNewScoreboard();
            obj = scoreboard.registerNewObjective("QuakeTokens", "dummy", "Quake Tokens");
            pregameTime = true;
            GetRespawns();
            GetPointsPlacement();
            GetPowerUpsPlacement();

        }
    }

    public void GameStart()
    {
        ChangeBarName("Good Luck!");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.DARK_AQUA + "Quake points");
        game_ID++;


        for(Player online : Bukkit.getOnlinePlayers())
        {
            if(players.contains(online))
            {
                online.setDisplayName(null);
                online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (QuakeConfig.Minutes*1200 + (QuakeConfig.Gameisabouttostarttime + 2) * 20), 2));
                online.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (QuakeConfig.Minutes*1200 + (QuakeConfig.Gameisabouttostarttime + 2) * 20), 1));

                online.setScoreboard(scoreboard);
                online.setGameMode(GameMode.ADVENTURE);
                quakePlayers.get(online).spawnEquipment();
                Respawn(online);
            }
        }

        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "The game will start in " + QuakeConfig.Gameisabouttostarttime + " seconds! Get to your positions!");
        //odliczanko
        for (int i = 5; i >=0; i--)
        {
            final int a = i;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run()
                {
                    if (a > 0) Bukkit.broadcastMessage(ChatColor.GREEN + String.valueOf(a) + "...");
                    else
                    {
                        Bukkit.broadcastMessage( ChatColor.RED + "The game has begun! May the best win!");
                        gameIsLive = true;
                        // Timer(game_ID);
                        Clock(QuakeConfig.Minutes * 60, game_ID);
                        PointsAndPowerUpsSpawner();
                    }
                }
            }, ((QuakeConfig.Gameisabouttostarttime - a) * 20));
        }
    }

    public void Clock(int time, int id)
    {
        if(time>=0)
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
            {
                public void run()
                {
                    int minutes = time / 60;
                    int seconds = time % 60;
                    ChangeBarName("Time Left - " + minutes + ":"  + String.format("%02d", seconds));

                    // koniec meczu
                    if (time == 0)
                    {
                        End();
                    }


                    // komunikat o nadchodzącym efekcie Super Vision
                    if (time == QuakeConfig.TimeBeforeGlowingPrompt)
                    {
                        Bukkit.broadcastMessage("Super Vision will be avaliable in 1 minute!");
                    }

                    // nadanie efektu Super Vision
                    if (time == QuakeConfig.TimeBeforeGlowing)
                    {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (players.contains(online)) {
                                online.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, QuakeConfig.TimeBeforeGlowing*20, 1));
                            }
                        }
                    }

                    // respawn Power Upów i punktów
                    if (time%40 == 0 && time!= QuakeConfig.Minutes * 60)
                    {
                        ItemStack dust = new ItemStack(Material.GLOWSTONE_DUST);
                        dust.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                        for(Entity i :Bukkit.getWorld(QuakeConfig.ArcadeWorldName).getEntities())
                        {
                            if(i instanceof Item)
                            {
                                Item item = (Item) i;
                                if(item.getItemStack().getItemMeta().hasEnchant(Enchantment.DIG_SPEED))i.remove();
                            }
                        }
                        for(Location loc : pointsLoc)
                        {
                            Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName).dropItemNaturally(loc, dust);
                        }

                        for(Player online : Bukkit.getOnlinePlayers())
                        {
                            if(players.contains(online)){
                                online.setFoodLevel(20);
                            }
                        }

                        ItemStack star = new ItemStack(Material.NETHER_STAR);
                        star.addUnsafeEnchantment(Enchantment.THORNS, 1);
                        for(Entity i :Bukkit.getWorld(QuakeConfig.ArcadeWorldName).getEntities())
                        {
                            if(i instanceof Item)
                            {
                                Item item = (Item) i;
                                if(item.getItemStack().getItemMeta().hasEnchant(Enchantment.THORNS))i.remove();
                            }
                        }
                        for(Location loc : powerUpLoc)
                        {
                            Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName).dropItemNaturally(loc, star);
                        }

                        for(Player online : Bukkit.getOnlinePlayers())
                        {
                            if(players.contains(online)){
                                online.setFoodLevel(20);
                            }
                        }
                    }

                    if (gameIsLive && id == game_ID) Clock(time-1, id);
                    else return;
                }
            }, (20));
        }
    }

    public void End()
    {
        gameIsLive= false;
        ChangeBarName("Congratz!");


        int seconds = 8;
        int max=0;
        Player p;
        p = Bukkit.getPlayer("Phoebe");

        for(Player online :Bukkit.getOnlinePlayers())
        {
            Score score = obj.getScore(online);
            if(score.getScore()>=max)
            {
                max = score.getScore();
                p = online;
            }
        }

        for(Entity i :Bukkit.getWorld(QuakeConfig.ArcadeWorldName).getEntities())
        {
            if(i instanceof Item)
            {
                Item item = (Item) i;
                ItemMeta it = item.getItemStack().getItemMeta();
                if(it.hasEnchant(Enchantment.DIG_SPEED) || it.hasEnchant(Enchantment.THORNS))i.remove();
            }
        }


        Score win_score = obj.getScore(p);

        for(Player online : Bukkit.getOnlinePlayers())
        {
            if(playersAlive.contains(online))
            {
                online.sendTitle(ChatColor.RED + "W" + ChatColor.GREEN + "i" + ChatColor.BLUE + "n" + ChatColor.YELLOW + "n" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.AQUA + "r " + ChatColor.WHITE + p.getName() + ChatColor.GOLD + "!", "with " + win_score.getScore() + " points! Congrats!");
                online.setGameMode(GameMode.SPECTATOR);
                Score score = obj.getScore(online);
                ItemStack emeralds = new ItemStack(Material.EMERALD, score.getScore() / 20 + 1);
                online.getInventory().addItem(emeralds);

                ItemStack[] armor = online.getInventory().getArmorContents();
                online.getInventory().addItem(armor[0]);
                online.getInventory().setBoots(null);

                // remove items from off hand if it is quake
                ItemStack i = online.getInventory().getItemInOffHand();
                removeSpecificItem(i, online);

                // removes quake items
                for (ItemStack j : online.getInventory().getContents())
                {
                    removeSpecificItem(j, online);
                }

            }




        }
        //nagroda dla zwycięzcy
        ItemStack diamonds = new ItemStack(Material.DIAMOND, win_score.getScore()/30 + 1);
        p.getInventory().addItem(diamonds);


        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
            public void run()
            {
                Location spawn = new Location(Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName), QuakeConfig.LobbySpawnCoordinates.x , QuakeConfig.LobbySpawnCoordinates.y, QuakeConfig.LobbySpawnCoordinates.z);
                ChangeBarName("Welcome to our server!");
                for(Player online : Bukkit.getOnlinePlayers())
                {
                    if(playersAlive.contains(online))
                    {
                        online.teleport(spawn);
                        online.setGameMode(GameMode.ADVENTURE);
                        scoreboard.resetScores(online);
                    }
                }
            }
        }, (seconds * 20));


        scoreboard.clearSlot(obj.getDisplaySlot());
        pregameTime = false;
        infoBar.removeAll();
    }

    public void Respawn(Player p)
    {
        int seconds = 1;
        int index = 0;
        double ran = Math.random();
        ran = ran * respawns.size();
        index = (int) ran;
        Location loc = respawns.get(index);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
            public void run()
            {
                p.teleport(loc);
            }
        }, (seconds * 20));
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event)
    {
        // Event Handler that registers unique projectiles in DataUtils class so it can be identified with its shooter
        if(gameIsLive)
        {
            // Gets source of projectile
            Entity shooter = event.getEntity();
            Player p = null;
            // Checks if it is a player
            if (shooter instanceof Player) {
                p = (Player) shooter;
                Entity projectile = event.getProjectile();
                // Adds projectile and all data to register of player's projectiles
                DataUtils.SetString(projectile, DataUtils.SHOT_BY, p.getUniqueId().toString());

            }
        }
    }

    @EventHandler
    public void onEntityPickUpItem(EntityPickupItemEvent event)
    {
        // Event Handler that grants player points if picked up item is quake dust
        if(gameIsLive)
        {
            // Gets player that picked up item
            Entity pickuper = event.getEntity();
            Player p = (Player) pickuper;

            // Checks if item is Glowstone Dust
            if (event.getItem().getItemStack().getType() == Material.GLOWSTONE_DUST)
            {
                ItemStack item = event.getItem().getItemStack();
                ItemMeta meta = item.getItemMeta();

                // Checks if item has Dig Speed, which is enchantment that identifies Glowstone Dust as Quake Dust
                if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
                    // Plays sound as confirmation
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    event.setCancelled(true);
                    // Removes item from game
                    event.getItem().remove();
                    int score = quakePlayers.get(p).addScore(QuakeConfig.PointsForDust);
                    // Checks if Player has earned enough points to win the game
                    if(score >= QuakeConfig.Max_Points)End();
                }
            }
            if (event.getItem().getItemStack().getType() == Material.NETHER_STAR)
            {
                ItemStack item = event.getItem().getItemStack();
                ItemMeta meta = item.getItemMeta();

                // Checks if item has Dig Speed, which is enchantment that identifies Glowstone Dust as Quake Dust
                if (meta.hasEnchant(Enchantment.THORNS))
                {
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    event.setCancelled(true);
                    // Removes item from game
                    event.getItem().remove();

                    powerUp.drawPowerUp(p);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntityEvent(EntityDamageByEntityEvent event)
    {
        // Event Handler
        if (gameIsLive)
        {
            // gets hit player and damager
            Entity target = event.getEntity();
            Entity damager = event.getDamager();
            Player deadguy = null;

            if(target instanceof Player)
            {
                deadguy = (Player) target;

                // if melee
                if (damager instanceof Player) {
                    Player shotBy = (Player) damager;
                    event.setCancelled(true);

                    // QuakePlayer method that handles being hit by a player
                    if (quakePlayers.get(deadguy).gotHit(shotBy))
                    {
                        quakePlayers.get(shotBy).incrementKillStreak();
                        // points for ending foe's kill spree
                        int killpoints = quakePlayers.get(deadguy).getKillpoints();
                        // points for killers kill spree
                        int killspree = (int) (Math.pow(quakePlayers.get(shotBy).getKillStreak(), 2) * 0.25);
                        // changing killers score
                        int score = quakePlayers.get(shotBy).addScore(QuakeConfig.PointsForKill + killpoints + killspree);
                        // checking if game winning conditions has been met
                        if (score >= QuakeConfig.Max_Points) End();
                        // respawning killed player
                        Respawn(deadguy);
                    }
                    return;
                }

                // if rocket hit
                if (damager instanceof Firework) {
                    Firework f = (Firework) damager;
                    ItemMeta meta = f.getFireworkMeta();
                    Player shotBy = getServer().getPlayer(UUID.fromString(DataUtils.GetString(damager, DataUtils.SHOT_BY, "")));

                    boolean hit = meta.hasEnchant(Enchantment.LUCK);
                    if (hit) {
                        if (shotBy != null) {
                            if (shotBy.equals(deadguy)) return;
                            event.setCancelled(true);

                            // QuakePlayer method that handles being hit by a player
                            if (quakePlayers.get(deadguy).gotHit(shotBy))
                            {
                                quakePlayers.get(shotBy).incrementKillStreak();
                                // points for ending foe's kill spree
                                int killpoints = quakePlayers.get(deadguy).getKillpoints();
                                // points for killers kill spree
                                int killspree = (int) (Math.pow(quakePlayers.get(shotBy).getKillStreak(), 2) * 0.25);
                                // changing killers score
                                int score = quakePlayers.get(shotBy).addScore(QuakeConfig.PointsForKill + killpoints + killspree);
                                // checking if game winning conditions has been met
                                if (score >= QuakeConfig.Max_Points) End();
                                // respawning killed player
                                Respawn(deadguy);
                            }
                        }
                    }
                }
            }
        }
    }

    public void Join(Player p)
    {
        // this method adds player to game lobby and give them game equipment
        if (!gameIsLive && pregameTime && !players.contains(p)) {
            Bukkit.broadcastMessage(ChatColor.AQUA + p.getName() + ChatColor.WHITE + " has just joined the game!");
            // resets score and ads player to players list, enables boss bar visibility for player and creating theirs equipment
            scoreboard.resetScores(p);
            players.add(p);
            playersAlive.add(p);
            quakePlayers.put(p, new QuakePlayer(p));
            infoBar.addPlayer(p);
        }
    }

    public void Leave(Player p)
    {
        if (playersAlive.contains(p)) {
            Location spawn = new Location(Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName), QuakeConfig.LobbySpawnCoordinates.x , QuakeConfig.LobbySpawnCoordinates.y, QuakeConfig.LobbySpawnCoordinates.z);
            Score score = obj.getScore(p);
            ItemStack emeralds = new ItemStack(Material.EMERALD, score.getScore() / 20 + 1);
            p.getInventory().addItem(emeralds);
            p.teleport(spawn);
            p.setGameMode(GameMode.ADVENTURE);
            if(players.contains(p))players.remove(p);
            playersAlive.remove(p);
            quakePlayers.remove(p);
            infoBar.removePlayer(p);
        }
    }


    public void PointsAndPowerUpsSpawner()
    {

        if(gameIsLive)
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
            {
                public void run()
                {
                    ItemStack dust = new ItemStack(Material.GLOWSTONE_DUST);
                    dust.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                    for(Entity i :Bukkit.getWorld(QuakeConfig.ArcadeWorldName).getEntities())
                    {
                        if(i instanceof Item)
                        {
                            Item item = (Item) i;
                            if(item.getItemStack().getItemMeta().hasEnchant(Enchantment.DIG_SPEED))i.remove();
                        }
                    }
                    for(Location loc : pointsLoc)
                    {
                        Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName).dropItemNaturally(loc, dust);
                    }

                    for(Player online : Bukkit.getOnlinePlayers())
                    {
                        if(players.contains(online)){
                            online.setFoodLevel(20);
                        }
                    }

                    ItemStack star = new ItemStack(Material.NETHER_STAR);
                    star.addUnsafeEnchantment(Enchantment.THORNS, 1);
                    for(Entity i :Bukkit.getWorld(QuakeConfig.ArcadeWorldName).getEntities())
                    {
                        if(i instanceof Item)
                        {
                            Item item = (Item) i;
                            if(item.getItemStack().getItemMeta().hasEnchant(Enchantment.THORNS))i.remove();
                        }
                    }
                    for(Location loc : powerUpLoc)
                    {
                        Bukkit.getServer().getWorld(QuakeConfig.ArcadeWorldName).dropItemNaturally(loc, star);
                    }

                    for(Player online : Bukkit.getOnlinePlayers())
                    {
                        if(players.contains(online)){
                            online.setFoodLevel(20);
                        }
                    }

                    PointsAndPowerUpsSpawner();
                }
            }, (QuakeConfig.DustRespawnTime * 20));
        }

    }

    public void GetRespawns()
    {
        int radius = QuakeConfig.ArenaRadius;
        World world = Bukkit.getWorld(QuakeConfig.ArcadeWorldName);
        Location loc = new Location(world,  QuakeConfig.ArenaCenterCoordinates.x, QuakeConfig.ArenaCenterCoordinates.y, QuakeConfig.ArenaCenterCoordinates.z);

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = world.getBlockAt(loc.getBlockX()+x, loc.getBlockY()+y, loc.getBlockZ()+z);
                    if (block.getType() == Material.DIAMOND_BLOCK)
                    {
                        Location l = new Location(world, block.getX(), block.getY() + 2, block.getZ());
                        respawns.add(l);
                    }
                }
            }
        }

    }

    public void GetPointsPlacement()
    {
        int radius = QuakeConfig.ArenaRadius;
        World world = Bukkit.getWorld(QuakeConfig.ArcadeWorldName);
        Location loc = new Location(world, QuakeConfig.ArenaCenterCoordinates.x, QuakeConfig.ArenaCenterCoordinates.y, QuakeConfig.ArenaCenterCoordinates.z);

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = world.getBlockAt(loc.getBlockX()+x, loc.getBlockY()+y, loc.getBlockZ()+z);
                    if (block.getType() == Material.BARRIER)
                    {
                        Location l = new Location(world, block.getX(), block.getY() + 2, block.getZ());
                        pointsLoc.add(l);
                    }
                }
            }
        }

    }

    public void GetPowerUpsPlacement()
    {
        int radius = QuakeConfig.ArenaRadius;
        World world = Bukkit.getWorld(QuakeConfig.ArcadeWorldName);
        Location loc = new Location(world, QuakeConfig.ArenaCenterCoordinates.x, QuakeConfig.ArenaCenterCoordinates.y, QuakeConfig.ArenaCenterCoordinates.z);

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = world.getBlockAt(loc.getBlockX()+x, loc.getBlockY()+y, loc.getBlockZ()+z);
                    if (block.getType() == Material.EMERALD_BLOCK)
                    {
                        Location l = new Location(world, block.getX(), block.getY() + 2, block.getZ());
                        powerUpLoc.add(l);
                    }
                }
            }
        }

    }

    public void ChangeBarName(String s)
    {
        infoBar.setTitle(s);
    }

    void removeSpecificItem(ItemStack i, Player p)
    {
        if (i != null)
        {
            if (i.hasItemMeta())
            {
                if (i.getItemMeta().hasDisplayName())
                {
                    String name = i.getItemMeta().getDisplayName();
                    if (name.equalsIgnoreCase(ChatColor.DARK_AQUA + "Papucie Zeskoku") || name.equalsIgnoreCase(ChatColor.GOLD + "Laserowa Dzida")
                            || name.equalsIgnoreCase("Wybuchowe szczurki na patyku"))
                    {
                        p.getInventory().remove(i);
                    }
                }
            }
        }
    }
}