package com.sakuratown.minions.minion;

import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.Main;
import com.sakuratown.minions.menu.ManagerMenu;
import com.sakuratown.minions.menu.StorageMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Set;

public class Minion {

    private final String type;
    private final int totalWeight;
    private final Set<String> collectItemList;
    private final ConfigurationSection config;

    private StorageMenu storageMenu;
    private ManagerMenu managerMenu;

    private int storage;
    private int efficiency;
    private String name = "EnTIv 的工人";

    public Minion(String type, int storage, int efficiency) {

        this.type = type;
        this.storage = storage;
        this.efficiency = efficiency;

        config = Config.getConfig("minions").getConfigurationSection(type);

        collectItemList = config.getConfigurationSection("CollectItemList").getKeys(false);
        totalWeight = getTotalWeight();

        setupMenu();
        collectItem();
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public void openStorageMenu(Player player) {
        storageMenu.open(player);
    }

    public void openManagerMenu(Player player) {
        managerMenu.open(player);
    }

    public void upgradeStorage(int row) {
        //TODO 扣钱
        this.storage += row;
    }

    public void upgradeEfficiency(int amount) {
        //TODO 扣钱
        this.efficiency += amount;
    }

    public void collectItem() {

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Material, Integer> collectItems = new HashMap<>();

                //TODO 如果效率 1000 要循环获取 1000 次物品, 降低循环次数
                for (int i = 0; i < efficiency; i++) {
                    Material randomMaterial = getRandomMaterial();
                    collectItems.merge(randomMaterial, 1, Integer::sum);
                }

                boolean isFull = storageMenu.addItem(collectItems);
                if (isFull)  cancel();
            }
        }.runTaskTimer(Main.getInstance(), 0, 2);

    }

    public void setName(String name) {
        //TODO 设置悬浮字
        this.name = name;
    }

    public void remove() {
        //TODO 移除工人
    }

    private void setupMenu() {
        managerMenu = new ManagerMenu(this);
        storageMenu = new StorageMenu(storage, this);
    }

    private Material getRandomMaterial() {

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        String randomMaterial = null;

        for (String material : collectItemList) {

            int weight = config.getInt("CollectItemList.".concat(material));
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

        for (String material : collectItemList) {
            int weight = config.getInt("CollectItemList.".concat(material));
            totalWeight += weight;
        }

        return totalWeight;
    }
}
