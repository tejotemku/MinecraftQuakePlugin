package com.quake;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class ItemUtils {
    public static String GetString(ItemStack item, String key, String def)
    {

        return DataUtils.GetString(item.getItemMeta(),key,def);
    }

    public static ItemStack SetString(ItemStack item, String key, String val)
    {
        ItemMeta meta = item.getItemMeta();
        DataUtils.SetString(meta, key, val);
        item.setItemMeta(meta);
        return item;
    }

    public static double GetDouble(ItemStack item, String key, double def)
    {
        return DataUtils.GetDouble(item.getItemMeta(), key, def);
    }

    public static ItemStack SetDouble(ItemStack item, String key, double val)
    {
        ItemMeta meta = item.getItemMeta();
        DataUtils.SetDouble(meta, key, val);
        item.setItemMeta(meta);
        return item;
    }

    public static int GetInt(ItemStack item, String key, int def)
    {
        return DataUtils.GetInt(item.getItemMeta(), key, def);
    }

    public static ItemStack SetInt(ItemStack item, String key, int val)
    {
        ItemMeta meta = item.getItemMeta();
        DataUtils.SetInt(meta, key, val);
        item.setItemMeta(meta);
        return item;
    }



    public static void WithMeta(ItemStack item, Consumer<ItemMeta> f)
    {
        ItemMeta meta = item.getItemMeta();
        f.accept(meta);
        item.setItemMeta(meta);
    }
}
