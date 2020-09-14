package com.quake;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class DataUtils {
    public static final String SHOT_BY = "ShotBy";

    public static NamespacedKey GetKey(String key)
    {
        return new NamespacedKey(QuakeMain.getInstance(), key);
    }

    public static String GetString(PersistentDataHolder item, String key, String def)
    {
        return item.getPersistentDataContainer().getOrDefault(GetKey("s_"+key), PersistentDataType.STRING , def);
    }

    public static PersistentDataHolder SetString(PersistentDataHolder item, String key, String val)
    {
        item.getPersistentDataContainer().set(GetKey("s_"+key), PersistentDataType.STRING, val);
        return item;
    }

    public static double GetDouble(PersistentDataHolder item, String key, double def)
    {
        return item.getPersistentDataContainer().getOrDefault(GetKey("d_"+key), PersistentDataType.DOUBLE , def);
    }

    public static PersistentDataHolder SetDouble(PersistentDataHolder item, String key, double val)
    {
        item.getPersistentDataContainer().set(GetKey("d_"+key), PersistentDataType.DOUBLE, val);
        return item;
    }

    public static int GetInt(PersistentDataHolder item, String key, int def)
    {
        return item.getPersistentDataContainer().getOrDefault(GetKey("i_"+key), PersistentDataType.INTEGER , def);
    }

    public static PersistentDataHolder SetInt(PersistentDataHolder item, String key, int val)
    {
        item.getPersistentDataContainer().set(GetKey("i_"+key), PersistentDataType.INTEGER, val);
        return item;
    }

    public static long GetLong(PersistentDataHolder item, String key, long def)
    {
        return item.getPersistentDataContainer().getOrDefault(GetKey("l_"+key), PersistentDataType.LONG, def);
    }

    public static PersistentDataHolder SetLong(PersistentDataHolder item, String key, long val)
    {
        item.getPersistentDataContainer().set(GetKey("l_"+key), PersistentDataType.LONG, val);
        return item;
    }

    public static boolean UseCooldown(PersistentDataHolder item, String key, int cooldown)
    {
        if(CanUse(item,key))
        {
            SetCooldown(item,key,cooldown);
            return true;
        }
        return false;
    }

    public static boolean CanUse(PersistentDataHolder item, String key)
    {
        long ticks = System.currentTimeMillis();
        return ticks >= GetLong(item, key, 0);
    }

    public static void SetCooldown(PersistentDataHolder item, String key, int cooldown)
    {
        SetLong(item, key, System.currentTimeMillis()+cooldown);
    }

}