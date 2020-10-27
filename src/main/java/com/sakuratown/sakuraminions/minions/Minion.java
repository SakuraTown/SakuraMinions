package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class Minion {

    String type;
    int size;
    int amount;

    Inventory inventory;

    public Minion(String type, int size, int amount) {
        this.type = type;
        this.size = size;
        this.amount = amount;

        inventory = Bukkit.createInventory(null, size, "工人背包");
    }

    void upgradeSize() {

    }

    void upgradeAmount() {

    }

    public void getSettingItem() {
        Main plugin = Main.getInstance();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection(type);
        System.out.println(config.getKeys(true));
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

}
