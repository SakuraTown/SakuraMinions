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
    private final ConfigurationSection config;

    public Minion(String type, int storage, int efficiency) {

        this.type = type;
        this.storage = storage;
        this.efficiency = efficiency;

        //TODO 每个工人 new 一个 config 好像不太对, 有没有办法统一获取呢?
        config = new Config("minions").getConfigurationSection(type);
        totalWeight = getTotalWeight();

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

        ConfigurationSection collectItemListConfig = getCollectItemList();
        Set<String> collectItemList = collectItemListConfig.getKeys(false);

        //TODO 弄个方法返回 runnable
        runnable = new BukkitRunnable() {
            @Override
            public void run() {

                long start = System.currentTimeMillis();
                HashMap<Material, Integer> collectItems = new HashMap<>();

                //TODO 当前运行 10 万次 14ms
                for (int i = 0; i < 100000; i++) {
                    Material randomMaterial = getRandomMaterial(collectItemList, collectItemListConfig);
                    collectItems.merge(randomMaterial, 1, Integer::sum);
                }

                long end = System.currentTimeMillis();
                long time = end - start;
                System.out.println("运行耗时: " + time + " ms");
                boolean isFull = storageMenu.addItem(collectItems);



                if (isFull) cancel();


            }
        };

        //TODO 防止重复运行任务
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

    private Material getRandomMaterial(Set<String> collectItemList, ConfigurationSection collectItemListConfig) {

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

    private int getTotalWeight() {

        int totalWeight = 0;

        for (String material : getCollectItemList().getKeys(false)) {
            int weight = getCollectItemList().getInt(material);
            totalWeight += weight;
        }

        return totalWeight;
    }

    private ConfigurationSection getCollectItemList() {
        return config.getConfigurationSection("CollectItemList");
    }

    public ConfigurationSection getConfig(){
        return config;
    }
}
