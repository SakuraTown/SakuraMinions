package com.sakuratown.sakuraminions.minions;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.inventory.InventoryType.PLAYER;

public class InventoryGUIListener implements Listener {
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) event.getWhoClicked()).getPlayer();
        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();
        MinionInventory gui = getGui(event);
        if (gui == null) return;
        if(event.getRawSlot() > gui.getCurrentInventory().getSize()-1){return;}
        int nowPage = gui.getNowPage();
        if (currentItem == null) {
            return;
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("LastPage.Name"))) {

            gui.showInventoryGUI(--nowPage, player);
            gui.setNowPage(nowPage);
            event.setCancelled(true);
            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("NextPage.Name"))) {

            gui.showInventoryGUI(++nowPage, player);
            gui.setNowPage(nowPage);
            event.setCancelled(true);
            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            event.setCancelled(true);
           // denyClick(event, inventory);
        }


    }
    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent event) { //功能不完整
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) event.getWhoClicked()).getPlayer();
        ItemStack currentItem = event.getCursor();
        MinionInventory gui = getGui(event);
        if (gui == null) return;
        int nowPage = gui.getNowPage();
        if (currentItem == null) {
            return;
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("LastPage.Name"))) {
            Bukkit.broadcastMessage("1");
            event.setCancelled(true);
            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("NextPage.Name"))) {
            Bukkit.broadcastMessage("2");
            event.setCancelled(true);
            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            Bukkit.broadcastMessage("3");
            event.setCancelled(true);
            // denyClick(event, inventory);
        }


    }

    private MinionInventory getGui(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }

    public static void denyClick(InventoryClickEvent event, Inventory targetInventory) {

        int size = targetInventory.getSize();

        if (event.getRawSlot() < size || event.isShiftClick()) {
            event.setCancelled(true);
        }
    }
}
