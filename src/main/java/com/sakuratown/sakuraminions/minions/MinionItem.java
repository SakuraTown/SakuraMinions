package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuralibrary.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;

public class MinionItem {
    public static ItemStack getMinionItem(String type, int size, int amount) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
        itemBuilder.name(ChatColor.GOLD + type);
        itemBuilder.lore(ChatColor.GREEN + "背包容量:" + ChatColor.LIGHT_PURPLE + size * 9 + ChatColor.GREEN + "格")
                .addLore(ChatColor.GREEN + "工作效率:" + ChatColor.LIGHT_PURPLE + amount + ChatColor.GREEN + "个/秒");
        return itemBuilder.BuildSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2MmExNWIwNDY5MmEyZTRiM2ZiMzY2M2JkNGI3ODQzNGRjZTE3MzJiOGViMWM3YTlmN2MwZmJmNmYifX19");
    }

}
