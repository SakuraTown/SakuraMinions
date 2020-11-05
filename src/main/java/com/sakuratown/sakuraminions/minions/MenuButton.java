package com.sakuratown.sakuraminions.minions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuButton {
    public static String type; //type Top或者Menu。
    public static ItemStack[] fistMenu;
    public static ItemStack[] midMenu;
    public static ItemStack[] lastMenu;
    public static ItemStack lockArea;
    private static ItemStack lastPage;
    private static ItemStack nextPage;
    private static ItemStack placeHolder;
    private static int[] lastPageSlots;
    private static int[] nextPageSlots;
    private static int[] placeHolderSlots;
    private static ConfigurationSection config;
    private static int totalSlot;

    public static void loadMenuButton() {
        type = Config.getMenuStyle();
        config = Config.getMenuSection();
        String lockAreaMaterial = config.getString("LockArea.Material");
        String lastPageMaterial = config.getString("LastPage.Material");
        String nextPageMaterial = config.getString("NextPage.Material");
        String placeHolderMaterial = config.getString("PlaceHolder.Material");

        String lastPageSlot = config.getString("LastPage.Slots");
        String nextPageSlot = config.getString("NextPage.Slots");
        String placeHolderSlot = config.getString("PlaceHolder.Slots");

        lastPageSlots = initSlot(lastPageSlot);
        nextPageSlots = initSlot(nextPageSlot);
        placeHolderSlots = initSlot(placeHolderSlot);

        lockArea = new ItemStack(Material.getMaterial(lockAreaMaterial),1);

        lastPage = new ItemStack(Material.getMaterial(lastPageMaterial),1);
        nextPage = new ItemStack(Material.getMaterial(nextPageMaterial),1);
        placeHolder = new ItemStack(Material.getMaterial(placeHolderMaterial),1);

        ItemMeta lockAreaMeta =  lockArea.getItemMeta();
        ItemMeta lastPageMeta =  lastPage.getItemMeta();
        ItemMeta nextPageMeta =  nextPage.getItemMeta();
        ItemMeta placeHolderMeta =  placeHolder.getItemMeta();

        lockAreaMeta.setDisplayName(config.getString("LockArea.Name"));
        lockArea.setItemMeta(lockAreaMeta);
        lastPageMeta.setDisplayName(config.getString("LastPage.Name"));
        lastPage.setItemMeta(lastPageMeta);
        nextPageMeta.setDisplayName(config.getString("NextPage.Name"));
        nextPage.setItemMeta(nextPageMeta);
        placeHolderMeta.setDisplayName(config.getString("PlaceHolder.Name"));
        placeHolder.setItemMeta(placeHolderMeta);

        totalSlot = lastPageSlots.length+nextPageSlots.length+placeHolderSlots.length;
        initFistMenu();
        initMidMenu();
        initEndMenu();
    }
    public static void initFistMenu(){
        int[] fistPlaceHolderSlots = merge(lastPageSlots,placeHolderSlots);
        fistMenu = new ItemStack[totalSlot];
        for(int n :fistPlaceHolderSlots){
            fistMenu[n-1] = placeHolder;
        }
        for(int n :nextPageSlots){
            fistMenu[n-1] = nextPage;
        }
    }
    public static void initMidMenu(){
        midMenu = new ItemStack[totalSlot];
        for(int n :lastPageSlots){
            midMenu[n-1] = lastPage;
        }
        for(int n :nextPageSlots){
            midMenu[n-1] = nextPage;
        }
        for(int n :placeHolderSlots){
            midMenu[n-1] = placeHolder;
        }
    }
    public static void initEndMenu(){
        lastMenu = new ItemStack[totalSlot];
        int[] endPlaceHolderSlots= merge(nextPageSlots,placeHolderSlots);
        for(int n :lastPageSlots){
            lastMenu[n-1] = lastPage;
        }
        for(int n :endPlaceHolderSlots){
            lastMenu[n-1] = placeHolder;
        }
    }

    private static int[] initSlot(String slotConfig) {
        int[] slots;
        if (slotConfig.contains(",")) {

            String[] split = slotConfig.split(",");
            slots = new int[split.length];

            for (int i = 0; i < split.length; i++) {
                slots[i] = Integer.parseInt(split[i]);
            }

        } else {
            slots = new int[1];
            slots[0] = Integer.parseInt(slotConfig);
        }
        return slots;
    }

    public static int[]merge(int[]a, int[]b){
        int[]c = new int[a.length+b.length];
        int i;
        for(i=0; i<a.length; i++)
            c[i] = a[i];
        for (int value : b) c[i++] = value;
        return c;
    }
}

