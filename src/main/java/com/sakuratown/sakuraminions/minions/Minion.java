package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Minion {

    String type;
    int row;
    int amount;

    Inventory inventory;

    public Minion(String type, int row, int amount) {
        this.type = type;
        this.row = row;
        this.amount = amount;

        inventory = Bukkit.createInventory(null, row * 9, "工人背包");
    }

    void upgradeSize(int row) {
        this.row += row;
    }

    void upgradeAmount(int amount) {
        this.amount += amount;
    }

    private void loadConfigItems() {

        Main plugin = Main.getInstance();

        ConfigurationSection config = plugin.getConfig();
        ConfigurationSection itemConfigurationSection = config.getConfigurationSection("Minions." + type + ".Item");

        if (itemConfigurationSection == null) return;

        Set<String> itemSet = itemConfigurationSection.getKeys(false);

        int totalWeight = 0;

        for (String material : itemSet) {
            int weight = itemConfigurationSection.getInt(material);
            totalWeight += weight;
        }

        String randomMaterial = getRandomMaterial(totalWeight, itemSet, itemConfigurationSection);
    }

    public String getRandomMaterial(int totalWeight, Set<String> itemSet, ConfigurationSection itemConfigurationSection) {

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        for (String material : itemSet) {

            int weight = itemConfigurationSection.getInt(material);
            chance += weight;

            System.out.println(chance);
            System.out.println(randomNum);

            if (randomNum <= chance) {
                return material;
            }
        }

        return null;
    }

}
