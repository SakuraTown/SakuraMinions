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

    private StorageMenu storageMenu;
    private ManagerMenu managerMenu;

    private int storage;
    private int efficiency;
    private String name = "EnTIv 的工人";

    private BukkitRunnable runnable;

    public Minion(String type, int storage, int efficiency) {

        this.type = type;
        this.storage = storage;
        this.efficiency = efficiency;

        setupMenu();
        collectItem();
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

        if (runnable != null) return;

        ConfigurationSection collectItemListConfig = getCollectItemList();
        Set<String> collectItemList = collectItemListConfig.getKeys(false);

        int totalWeight = getTotalWeight(collectItemListConfig);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {

                HashMap<Material, Integer> collectItems = new HashMap<>();

                //TODO 当前运行 10 万次 14ms, 时间复杂度 O(n), 有机会优化一下
                for (int i = 0; i < 64 * 6; i++) {
                    Material randomMaterial = getRandomMaterial(collectItemList, collectItemListConfig, totalWeight);
                    collectItems.merge(randomMaterial, 1, Integer::sum);
                }

                boolean isFull = storageMenu.addItem(collectItems);

                if (isFull) {
                    cancel();
                    runnable = null;
                }
            }
        };

        int collectTime = Main.getInstance().getConfig().getInt("CollectTime");
        runnable.runTaskTimer(Main.getInstance(), 0, 5);

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

    private Material getRandomMaterial(Set<String> collectItemList, ConfigurationSection collectItemListConfig, int totalWeight) {

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        String randomMaterial = null;

        for (String material : collectItemList) {

            int weight = collectItemListConfig.getInt(material);
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

    private int getTotalWeight(ConfigurationSection collectItemList) {

        int totalWeight = 0;

        for (String material : collectItemList.getKeys(false)) {
            int weight = collectItemList.getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }

    private ConfigurationSection getCollectItemList() {
        return getConfig().getConfigurationSection("CollectItemList");
    }

    public ConfigurationSection getConfig() {
        return Config.getConfig("minions").getConfigurationSection(type);
    }
}
