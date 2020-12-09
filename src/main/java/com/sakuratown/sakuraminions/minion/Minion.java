package com.sakuratown.sakuraminions.minion;

import com.sakuratown.sakuralibrary.utils.Config;
import com.sakuratown.sakuralibrary.utils.Message;
import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.menu.ManagerMenu;
import com.sakuratown.sakuraminions.menu.StorageMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Minion {

    private final ConfigurationSection section;
    private final Set<String> collectItemSet;

    StorageMenu storageMenu;
    ManagerMenu managerMenu;

    private int storage;
    private int efficiency;
    private int totalWeight;

    public Minion(String type, int storage, int efficiency) {

        this.storage = storage;
        this.efficiency = efficiency;

        storageMenu = new StorageMenu(type, storage, efficiency);

        section = Config.getConfigurationSection(type);
        collectItemSet = section.getKeys(false);

        setTotalWeight(section, collectItemSet);
    }

    public void openStorageMenu(Player player) {
        storageMenu.open(player);
    }

    public void openManagerMenu(Player player) {

    }

    public void upgradeStorage(int row) {
        this.storage += row;
    }

    public void upgradeEfficiency(int amount) {
        this.efficiency += amount;
    }

    public void collectItem() {

        List<ItemStack> collectItems = new ArrayList<>();

        for (int i = 0; i < efficiency; i++) {
            Material randomMaterial = getRandomMaterial();
            ItemStack itemStack = new ItemStack(randomMaterial, 1);

            collectItems.add(itemStack);
        }

        storageMenu.add(collectItems);
    }

    private Material getRandomMaterial() {

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        String randomMaterial = null;

        for (String material : collectItemSet) {

            int weight = section.getInt(material);
            chance += weight;

            if (randomNum <= chance) {
                randomMaterial = material;
                break;
            }
        }

        if (randomMaterial == null) {
            return Material.STONE;
        } else {
            return Material.getMaterial(randomMaterial);
        }

    }

    private void setTotalWeight(ConfigurationSection section, Set<String> collectItemSet) {

        int totalWeight = 0;

        for (String material : collectItemSet) {
            int weight = section.getInt(material);
            totalWeight += weight;
        }

        this.totalWeight = totalWeight;
    }
}
