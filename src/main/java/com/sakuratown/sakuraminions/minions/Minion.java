package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class Minion{

    String type;
    int row;
    int amount;
    MinionInventory inventories;
    InventoryGUI inventoryGUI;

    public Minion(String type, int row, int amount) {

        this.type = type;
        this.row = row;
        this.amount = amount;
        inventories = new MinionInventory(type,row);
        inventoryGUI = new InventoryGUI(inventories);
    }
    public void showGuI(int page,Player player){
        inventoryGUI.showInventoryGUI(page,player);
    }
    public String getRandomMaterial() {

        Main plugin = Main.getInstance();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection configurationSection = config.getConfigurationSection("Minions." + type + ".Item");

        if (configurationSection == null) {
            throw new NullPointerException("配置文件有误, 请检查配置文件");
        }

        Set<String> itemSet = configurationSection.getKeys(false);

        int totalWeight = getTotalWeight(configurationSection, itemSet);

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

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

    private int getTotalWeight(ConfigurationSection configurationSection, Set<String> itemSet) {

        int totalWeight = 0;

        for (String material : itemSet) {
            int weight = configurationSection.getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }

}
