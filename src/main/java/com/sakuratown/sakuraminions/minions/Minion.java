package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

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

    public String getRandomMaterial() {

        Main plugin = Main.getInstance();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection configurationSection = config.getConfigurationSection("Minions." + type + ".Item");

        int totalWeight = getTotalWeight(configurationSection);

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        Set<String> itemSet = configurationSection.getKeys(false);

        String randomMaterial = null;

        for (String material : itemSet) {

            int weight = configurationSection.getInt(material);
            chance += weight;

            if (randomNum <= chance) {
                randomMaterial = material;
                break;
            }

        }

        return randomMaterial;
    }

    void upgradeSize(int row) {
        this.row += row;
    }

    void upgradeAmount(int amount) {
        this.amount += amount;
    }

    private int getTotalWeight(ConfigurationSection configurationSection) {

        if (configurationSection == null) {
            throw new NullPointerException("配置文件有误, 请检查配置文件");
        }

        Set<String> itemSet = configurationSection.getKeys(false);

        int totalWeight = 0;

        for (String material : itemSet) {
            int weight = configurationSection.getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }

}
