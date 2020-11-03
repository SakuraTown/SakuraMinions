package com.sakuratown.sakuraminions.minions;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class InventoryGUIListener implements Listener {

    public static void denyClick(InventoryClickEvent event, Inventory targetInventory) {

        int size = targetInventory.getSize();

        if (event.getRawSlot() < size || event.isShiftClick()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {

        Player player = ((Player) event.getWhoClicked()).getPlayer();
        if (player == null) return;

        ItemStack currentItem = event.getCurrentItem();
        MinionInventory gui = getGui(event);

        if (gui == null) return;

        Inventory inventory = gui.getCurrentInventory();

        if (event.getRawSlot() > inventory.getSize() - 1) {
            return;
        }

        int nowPage = gui.getNowPage();

        if (currentItem == null) {
            return;
        }

        String displayName = currentItem.getItemMeta().getDisplayName();

        // config 类返回一个 itemStack 然后在这里比较它不香吗? 这样判断有点繁琐噢
        if (displayName.equals(Config.getMenuSection().getString("LastPage.Name"))) {
            gui.showInventoryGUI(--nowPage, player);
            gui.setNowPage(nowPage);
            event.setCancelled(true);
        }

        if (displayName.equals(Config.getMenuSection().getString("NextPage.Name"))) {
            gui.showInventoryGUI(++nowPage, player);
            gui.setNowPage(nowPage);
            event.setCancelled(true);
        }

        if (displayName.equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            event.setCancelled(true);
        }


    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        MinionInventory gui = getGui(event);
        if (gui == null) return;
        gui.setCurrentInventory(event.getInventory());
    }

    private MinionInventory getGui(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }
}
