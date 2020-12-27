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

        totalWeight = getTotalWeight();

        setupMenu();
        collectItem();
    }

    public ConfigurationSection getConfig() {
        return Config.getConfig("minions").getConfigurationSection(type);
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


        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                HashMap<Material, Integer> collectItems = new HashMap<>();

                //TODO 性能瓶颈 循环 1000 次 耗时 1000ms
                for (int i = 0; i < 100; i++) {
                    Material randomMaterial = getRandomMaterial(getCollectItemList().getKeys(false));
                    collectItems.merge(randomMaterial, 1, Integer::sum);
                }

                boolean isFull = storageMenu.addItem(collectItems);
                if (isFull) cancel();

                long end = System.currentTimeMillis();
                long time = end - start;
                System.out.println("运行耗时: " + time + " ms");

            }
        };

        //TODO 如果仓库未满, 则不执行
        runnable.runTaskTimer(Main.getInstance(), 0, 20);

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

    private Material getRandomMaterial(Set<String> collectItemList) {

        int chance = 0;
        double randomNum = Math.random() * totalWeight;

        String randomMaterial = null;

        for (String material : collectItemList) {

            int weight = getCollectItemList().getInt(material);
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

        for (String material : getCollectItemList().getKeys(false)) {
            int weight = getCollectItemList().getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }

    private ConfigurationSection getCollectItemList() {
        return getConfig().getConfigurationSection("CollectItemList");
    }
}
