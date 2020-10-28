package com.sakuratown.sakuraminions.minions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class MinionInitInventory implements Listener {
    public static ArrayList<Inventory> MinionInitInventory(InventoryHolder holder, int row) {
        ArrayList<Inventory> inventories = new ArrayList<>();
        if (row <= 6) {
            Inventory inventory = Bukkit.createInventory(holder, row * 9, "工人背包");
            inventories.add(inventory);
            return inventories;
        } else {
            int page = row / 6 + 1;
            for (int i = 1; i < page; i++) {
                Inventory inventory = Bukkit.createInventory(holder, 54, "工人背包#" + i);
                if (i == 1) {
                    addMenu(inventory, 0);
                }else{
                    addMenu(inventory,1);
                }
                inventories.add(inventory);

            }
            Inventory inventory = Bukkit.createInventory(holder, (row%6)+page-1, "工人背包#" + page);
            addMenu(inventory, 2);
            inventories.add(inventory);
        }
        return inventories;
    }

    public static void addMenu(Inventory inv, int i) { //i 为0时为第一页 为2的时候最后一页 为1或者其他任意值的时候中间页
        ItemStack turnPage = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        ItemMeta turnPageMeta = turnPage.getItemMeta();
        turnPageMeta.setDisplayName("§d 下一页");
        turnPage.setItemMeta(turnPageMeta);
        turnPageMeta.setDisplayName("§d 上一页");
        ItemStack placeHolder = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta placeHolderMeta = placeHolder.getItemMeta();
        placeHolderMeta.setDisplayName(" ");
        placeHolder.setItemMeta(placeHolderMeta);
        switch (i) {
            case 0:  //第一页 只有下一页的选项
                inv.setItem(53, turnPage);
                for (int j = 45; j <= 52; j++) {
                    inv.setItem(j, placeHolder);
                }
            case 2: //最后一页 只有上一页的选项
                turnPage.setItemMeta(turnPageMeta);
                inv.setItem(45, turnPage);
                for (int j = 46; j <= 53; j++) {
                    inv.setItem(j, placeHolder);
                }
            default: //中间页 有上一页和下一页
                inv.setItem(53, turnPage);
                turnPage.setItemMeta(turnPageMeta);
                inv.setItem(45, turnPage);
                for (int j = 46; j <= 52; j++) {
                    inv.setItem(j, placeHolder);
                }
        }
    }

}
