package com.sakuratown.sakuraminions.minions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.inventory.ItemStack;

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
        String lockAreaName = config.getString("LockArea.Name");
        String lastPageName = config.getString("LastPage.Name");
        String nextPageName = config.getString("NextPage.Name");
        String placeHolderName = config.getString("PlaceHolder.Name");

        String lastPageSlot = config.getString("LastPage.Slots");
        String nextPageSlot = config.getString("NextPage.Slots");
        String placeHolderSlot = config.getString("PlaceHolder.Slots");

        lastPageSlots = initSlot(lastPageSlot);
        nextPageSlots = initSlot(nextPageSlot);
        placeHolderSlots = initSlot(placeHolderSlot);

        lockArea = new ItemStack(Material.getMaterial(lockAreaName),1);
        lastPage = new ItemStack(Material.getMaterial(lastPageName),1);
        nextPage = new ItemStack(Material.getMaterial(nextPageName),1);
        placeHolder = new ItemStack(Material.getMaterial(placeHolderName),1);
        totalSlot = lastPageSlots.length+nextPageSlots.length+placeHolderSlots.length;
        initFistMenu();
        initMidMenu();
        initEndMenu();
    }
    public static void initFistMenu(){
        int[] fistPlaceHolderSlots = merge(lastPageSlots,placeHolderSlots);
        fistMenu = new ItemStack[totalSlot];
        for(int n :fistPlaceHolderSlots){
            fistMenu[n] = placeHolder;
        }
        for(int n :nextPageSlots){
            fistMenu[n] = nextPage;
        }
    }
    public static void initMidMenu(){
        midMenu = new ItemStack[totalSlot];
        for(int n :lastPageSlots){
            midMenu[n] = lastPage;
        }
        for(int n :nextPageSlots){
            midMenu[n] = nextPage;
        }
        for(int n :placeHolderSlots){
            midMenu[n] = placeHolder;
        }
    }
    public static void initEndMenu(){
        lastMenu = new ItemStack[totalSlot];
        int[] endPlaceHolderSlots= merge(nextPageSlots,placeHolderSlots);
        for(int n :lastPageSlots){
            lastMenu[n] = lastPage;
        }
        for(int n :endPlaceHolderSlots){
            lastMenu[n] = placeHolder;
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
        System.out.println(i);
        for (int value : b) c[i++] = value;
        return c;
    }
}

