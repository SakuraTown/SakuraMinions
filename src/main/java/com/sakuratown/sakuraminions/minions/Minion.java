package com.sakuratown.sakuraminions.minions;

import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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
        inventoryGUI.setInventories(inventories);
        inventoryGUI.showInventoryGUI(page,player);
    }
    public String getRandomMaterial() {

        ConfigurationSection configurationSection = Config.getMinionSection().getConfigurationSection(type + ".Item");
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

    public void upgradeSize(int row) {
        inventories.addRow(row);
        this.row += row;
    }
    public MinionInventory getMinionInventory(){
        return inventories;
    }
    void upgradeAmount(int amount) {
        this.amount += amount;
    }
    public void addItem(ItemStack item){
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(item);
        inventories.addItem(itemStacks);
    }
    public void addItem(ArrayList<ItemStack> itemStacks){
        inventories.addItem(itemStacks);
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
