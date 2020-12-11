package com.sakuratown.sakuraminions.minion;

import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.sakuraminions.menu.ManagerMenu;
import com.sakuratown.sakuraminions.menu.StorageMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
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
        section = Config.getConfig("minions").getConfigurationSection(type);
        collectItemSet = section.getKeys(false);

        totalWeight = getTotalWeight();

        setupMenu();
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

        //TODO 如果效率 1000 要循环 1000 次, 降低循环次数
        for (int i = 0; i < efficiency; i++) {
            Material randomMaterial = getRandomMaterial();

            ItemStack itemStack = new ItemStack(randomMaterial, 1);

            collectItems.add(itemStack);
        }

        storageMenu.addItem(collectItems);
    }

    private void setupMenu() {
        Config config = Config.getConfig("menu");
        managerMenu = (ManagerMenu) config.getMenu("管理菜单");
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

    private int getTotalWeight() {

        int totalWeight = 0;

        for (String material : collectItemSet) {
            int weight = section.getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }
}
